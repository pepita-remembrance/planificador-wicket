package edu.unq.uis.planificador.wicket.empleado

import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.domain.calendar.{DiaDeSemana, RecurrentCalendarSpace}
import edu.unq.uis.planificador.wicket.widgets.SubPanel
import edu.unq.uis.planificador.wicket.widgets.grid.columns.RequiredEditableDropDownColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.model.Model
import org.wicketstuff.egrid.column.RequiredEditableTextFieldColumn
import org.wicketstuff.egrid.provider.{EditableListDataProvider, IEditableDataProvider}

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

class DisponibilidadEmpleadoPanel(id: String, empleadoSeleccionado: Empleado) extends SubPanel(id, empleadoSeleccionado, classOf[RecurrentCalendarSpace]) {
  override def getColumns: mutable.Buffer[PropertyColumn[RecurrentCalendarSpace, String]] = {
    mutable.Buffer.empty[PropertyColumn[RecurrentCalendarSpace, String]] :+
      new RequiredEditableDropDownColumn[RecurrentCalendarSpace, String](new Model("Dia"), "diaDeSemana", diasRestantes) :+
      new RequiredEditableTextFieldColumn[RecurrentCalendarSpace, String](new Model("Inicio"), "inicio") :+
      new RequiredEditableTextFieldColumn[RecurrentCalendarSpace, String](new Model("Fin"), "fin")
  }

  override def getProvider: IEditableDataProvider[RecurrentCalendarSpace, String] = new DisponibilidadesProvider(empleadoSeleccionado)

  def diasRestantes: Seq[DiaDeSemana with Product] = {
    DiaDeSemana.todos.diff(empleadoSeleccionado.disponibilidades.map(_.diaDeSemana))
  }
}

