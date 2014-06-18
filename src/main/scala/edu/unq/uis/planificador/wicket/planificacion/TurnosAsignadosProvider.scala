package edu.unq.uis.planificador.wicket.planificacion

import edu.unq.uis.planificador.domain.{TurnoEmpleado, Planificacion}
import org.wicketstuff.egrid.provider.EditableListDataProvider
import scala.collection.JavaConversions._

class TurnosAsignadosProvider(planificacion: Planificacion) extends EditableListDataProvider[TurnoEmpleado, String]() {
  override def add(item: TurnoEmpleado) {
    //El add es más complejo y se tiene que impementar por separado
  }

  override def remove(item: TurnoEmpleado) {
    planificacion.empleados.find(_.nombreCompleto == item.nombre) match {
      case Some(empleado) => planificacion.quitarAsignacionA(empleado)
      case _ =>
    }
  }

  override def getData = planificacion.turnos
}
