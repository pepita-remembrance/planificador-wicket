package edu.unq.uis.planificador.wicket.empleado

import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.domain.calendar.{DiaDeSemana, RecurrentCalendarSpace}
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.{IModel, Model}
import org.wicketstuff.egrid.EditableGrid
import org.wicketstuff.egrid.column.{AbstractEditablePropertyColumn, EditableCellPanel, EditableRequiredDropDownCellPanel, RequiredEditableTextFieldColumn}
import org.wicketstuff.egrid.provider.EditableListDataProvider

import scala.collection.JavaConversions._
import scala.collection.mutable

class DisponibilidadesProvider(empleado: Empleado) extends EditableListDataProvider[RecurrentCalendarSpace, String]() {
  override def add(item: RecurrentCalendarSpace) {
    empleado.disponibleLos(item)
  }

  override def remove(item: RecurrentCalendarSpace) {
    empleado.borrarDisponibilidad(item)
  }

  override def getData = empleado.disponibilidades
}

class DisponibilidadEmpleadoPanel(var empleadoSeleccionado: Empleado) extends Panel("disponibilidades", new Model(empleadoSeleccionado)) {
  val ROWS_PER_PAGE = 20

  add(new EditableGrid[RecurrentCalendarSpace, String](
    "grid",
    getColumns,
    new DisponibilidadesProvider(empleadoSeleccionado),
    ROWS_PER_PAGE,
    classOf[RecurrentCalendarSpace]
  )
  )

  def setModel(model: IModel[Empleado]) {
    empleadoSeleccionado = model.getObject
  }

  def getColumns: mutable.Buffer[PropertyColumn[RecurrentCalendarSpace, String]] = {
    mutable.Buffer.empty[PropertyColumn[RecurrentCalendarSpace, String]] :+
      new RequiredEditableDropDownColumn[RecurrentCalendarSpace, String](new Model("Dia"), "diaDeSemana", diasRestantes) :+
      new RequiredEditableTextFieldColumn[RecurrentCalendarSpace, String](new Model("Inicio"), "inicio") :+
      new RequiredEditableTextFieldColumn[RecurrentCalendarSpace, String](new Model("Fin"), "fin")
  }

  def diasRestantes: Seq[DiaDeSemana with Product] = {
    DiaDeSemana.todos.diff(empleadoSeleccionado.disponibilidades.map(_.diaDeSemana))
  }
}

class RequiredEditableDropDownColumn[T, S](model: IModel[String], propertyExpression: String, values: Iterable[Any]) extends AbstractEditablePropertyColumn[T, S](model, propertyExpression) {
  override def getEditableCellPanel(componentId: String): EditableCellPanel = {
    new EditableRequiredDropDownCellPanel[T, S](componentId, this, values.toList)
  }
}