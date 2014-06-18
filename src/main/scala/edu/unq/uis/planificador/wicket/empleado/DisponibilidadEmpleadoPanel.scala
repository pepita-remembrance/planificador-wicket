package edu.unq.uis.planificador.wicket.empleado

import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.domain.calendar.{DiaDeSemana, RecurrentCalendarSpace}
import edu.unq.uis.planificador.wicket.widgets.grid.BootstrapEditableGrid
import edu.unq.uis.planificador.wicket.widgets.grid.columns.RequiredEditableDropDownColumn
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.{IModel, Model}
import org.wicketstuff.egrid.column.RequiredEditableTextFieldColumn
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

  add(createGrid)

  def createGrid = new BootstrapEditableGrid[RecurrentCalendarSpace, String](
    "grid",
    getColumns,
    new DisponibilidadesProvider(empleadoSeleccionado),
    ROWS_PER_PAGE,
    classOf[RecurrentCalendarSpace]
  ) {
    override def onDelete(target: AjaxRequestTarget, rowModel: IModel[RecurrentCalendarSpace]) = {
      empleadoSeleccionado.borrarDisponibilidad(rowModel.getObject)
      refreshGrid(target)
    }

    override def onAdd(target: AjaxRequestTarget, newRow: RecurrentCalendarSpace) {
      refreshGrid(target)
    }
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

  def refreshGrid(target: AjaxRequestTarget) {
    removeAll()
    add(createGrid)

    target add this
  }
}

