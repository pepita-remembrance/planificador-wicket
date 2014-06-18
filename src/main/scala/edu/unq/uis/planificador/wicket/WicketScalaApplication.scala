package edu.unq.uis.planificador.wicket

import de.agilecoders.wicket.core.Bootstrap
import de.agilecoders.wicket.core.settings.BootstrapSettings
import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.domain.builders.RecurrentCalendarSpaceBuilder._
import edu.unq.uis.planificador.domain.calendar.DiaDeSemana
import edu.unq.uis.planificador.domain.calendar.DiaDeSemana._
import edu.unq.uis.planificador.domain.disponibilidad.Turno
import edu.unq.uis.planificador.homes.{AbstractCollectionBasedHomeEmpleado, EmpleadosCollectionBasedHome}
import edu.unq.uis.planificador.wicket.converters.{DateTimeConverter, DiaDeSemanaConverter}
import edu.unq.uis.planificador.wicket.empleado.EmpleadosPage
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.{ConverterLocator, IConverterLocator}
import org.joda.time.DateTime

class WicketScalaApplication extends WebApplication {

  override def getHomePage = classOf[EmpleadosPage]

  override def init() {
    super.init()
    setupFixture()
    Bootstrap.install(this, new BootstrapSettings())
  }

  override def newConverterLocator: IConverterLocator = {
    val locator = super.newConverterLocator().asInstanceOf[ConverterLocator]
    locator.set(classOf[DiaDeSemana], new DiaDeSemanaConverter())
    locator.set(classOf[DateTime], new DateTimeConverter())
    locator
  }

  def setupFixture() {
    val empleadoHome: AbstractCollectionBasedHomeEmpleado = EmpleadosCollectionBasedHome

    val pedro = new Empleado("Pedro", "Picapiedras", "123134")
    pedro disponibleLos (Lunes de 14 a 19)
    pedro disponibleLos (Miercoles de 16 a 20)
    pedro restriccionEl "2014-06-22"
    pedro restriccionEl "2014-06-23"
    pedro asignar (Turno el "2014-06-18" de 14 a 18)
    empleadoHome.create(pedro)

    val juana = new Empleado("Juana", "De Arco", "8795468")
    juana disponibleLos (Miercoles de 9 a 15)
    juana disponibleLos (Viernes de 9 a 22)
    juana disponibleLos (Sabado de 16 a 20)
    pedro asignar (Turno el "2014-06-18" de 10 a 14)
    empleadoHome.create(juana)
  }

}