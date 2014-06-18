package edu.unq.uis.planificador.wicket.empleado

import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.domain.calendar.{CalendarElement, DiaDeSemana}
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.model.Model
import org.wicketstuff.egrid.column.RequiredEditableTextFieldColumn
import org.wicketstuff.egrid.provider.{EditableListDataProvider, IEditableDataProvider}

import scala.collection.JavaConversions._
import scala.collection.mutable

class RestriccionesProvider(empleado: Empleado) extends EditableListDataProvider[CalendarElement, String]() {
  override def add(item: CalendarElement) {
  }

  override def remove(item: CalendarElement) {
    empleado.borrarRestriccion(item)
  }

  override def getData = empleado.restricciones
}

class RestriccionesEmpleadoPanel(id: String, empleadoSeleccionado: Empleado) extends EmpleadosSubPanel(id, empleadoSeleccionado, classOf[CalendarElement]) {
  override def getColumns: mutable.Buffer[PropertyColumn[CalendarElement, String]] = {
    mutable.Buffer.empty[PropertyColumn[CalendarElement, String]] :+
      new RequiredEditableTextFieldColumn[CalendarElement, String](new Model("Dia"), "fechaUserFriendly")
  }

  override def getProvider: IEditableDataProvider[CalendarElement, String] = new RestriccionesProvider(empleadoSeleccionado)

  def diasRestantes: Seq[DiaDeSemana with Product] = {
    DiaDeSemana.todos.diff(empleadoSeleccionado.disponibilidades.map(_.diaDeSemana))
  }
}

