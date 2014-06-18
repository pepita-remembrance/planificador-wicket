package edu.unq.uis.planificador.wicket

import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar.ComponentPosition
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.{Navbar, NavbarButton, NavbarComponents}
import edu.unq.uis.planificador.wicket.empleado.EmpleadosPage
import edu.unq.uis.planificador.wicket.planificacion.PlanificacionesPage
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.Model

abstract class BasePage extends WebPage {
  add(newNavbar("navbar"))

  def newNavbar(id: String) = {
    val navbar = new Navbar(id)
    navbar.fluid()
    navbar.brandName(Model.of("Planificador"))

    navbar.addComponents(NavbarComponents.transform(ComponentPosition.LEFT,
      new NavbarButton(classOf[EmpleadosPage], Model.of("Empleados")).setIconType(GlyphIconType.user),
      new NavbarButton(classOf[PlanificacionesPage], Model.of("Planificaciones")).setIconType(GlyphIconType.calendar)
    ))

    navbar
  }
}