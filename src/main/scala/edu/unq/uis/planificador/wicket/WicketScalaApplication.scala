package edu.unq.uis.planificador.wicket

import java.util.Locale

import de.agilecoders.wicket.core.Bootstrap
import de.agilecoders.wicket.core.settings.BootstrapSettings
import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.domain.builders.RecurrentCalendarSpaceBuilder._
import edu.unq.uis.planificador.domain.calendar.DiaDeSemana
import edu.unq.uis.planificador.domain.calendar.DiaDeSemana.{Lunes, Martes, Sabado, Viernes}
import edu.unq.uis.planificador.domain.disponibilidad.Turno
import edu.unq.uis.planificador.homes.{AbstractCollectionBasedHomeEmpleado, EmpleadosCollectionBasedHome}
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.util.convert.IConverter
import org.apache.wicket.{ConverterLocator, IConverterLocator}

class WicketScalaApplication extends WebApplication {

  override def getHomePage = classOf[MainPage]

  override def init() {
    super.init()
    setupFixture()
    Bootstrap.install(this, new BootstrapSettings())
  }

  override def newConverterLocator: IConverterLocator = {
    val locator = super.newConverterLocator().asInstanceOf[ConverterLocator]
    locator.set(classOf[DiaDeSemana], new DiaDeSemanaConverter())
    locator
  }

  def setupFixture() {
    val empleadoHome: AbstractCollectionBasedHomeEmpleado = EmpleadosCollectionBasedHome

    val pedro = new Empleado("Pedro", "Picapiedras", "123134")
    pedro disponibleLos (Lunes de 14 a 19)
    pedro disponibleLos (Martes de 16 a 20)
    pedro restriccionEl "2014-04-25"
    pedro restriccionEl "2014-04-22"
    pedro asignar (Turno el "2014-04-21" de 14 a 18)
    empleadoHome.create(pedro)

    val juana = new Empleado("juana", "Picapiedras", "8795468")
    juana disponibleLos (Viernes de 9 a 22)
    juana disponibleLos (Sabado de 16 a 20)
    empleadoHome.create(juana)
  }

}

class DiaDeSemanaConverter extends IConverter[DiaDeSemana] {
  override def convertToObject(p1: String, p2: Locale): DiaDeSemana = DiaDeSemana.todos.find(_.nombre == p1).get

  override def convertToString(p1: DiaDeSemana, p2: Locale): String = p1.nombre
}