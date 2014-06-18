package edu.unq.uis.planificador.wicket.empleado

import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.wicket.widgets.SubPanel
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.model.Model
import org.joda.time.DateTime
import org.wicketstuff.egrid.column.RequiredEditableTextFieldColumn
import org.wicketstuff.egrid.provider.{EditableListDataProvider, IEditableDataProvider}
import edu.unq.uis.planificador.domain._
import scala.collection.JavaConversions._
import scala.collection.mutable
import edu.unq.uis.planificador.domain.calendar.{AllDayCalendarSpace, CalendarElement}

case class Restriccion(var fecha: DateTime) {
  def this() = this(null)
}

class RestriccionesProvider(empleado: Empleado) extends EditableListDataProvider[Restriccion, String]() {
  override def add(item: Restriccion) {
    empleado.restriccionEl(item.fecha)
  }

  override def remove(item: Restriccion) {
     empleado.borrarRestriccion(CalendarElement(disponibilidad.Restriccion, AllDayCalendarSpace(item.fecha)))
  }

  override def getData = empleado.restricciones.map(it => Restriccion(it.calendarSpace.fecha))
}

class RestriccionesEmpleadoPanel(id: String, empleadoSeleccionado: Empleado) extends SubPanel(id, empleadoSeleccionado, classOf[Restriccion]) {
  override def getColumns: mutable.Buffer[PropertyColumn[Restriccion, String]] = {
    mutable.Buffer.empty[PropertyColumn[Restriccion, String]] :+
      new RequiredEditableTextFieldColumn[Restriccion, String](new Model("Dia"), "fecha")
  }

  override def getProvider: IEditableDataProvider[Restriccion, String] = new RestriccionesProvider(empleadoSeleccionado)
}

