package edu.unq.uis.planificador.wicket

import de.agilecoders.wicket.core.Bootstrap
import de.agilecoders.wicket.core.settings.BootstrapSettings
import org.apache.wicket.protocol.http.WebApplication

class WicketScalaApplication extends WebApplication {

  override def getHomePage = classOf[MainPage]

  override def init() {
    super.init()
    Bootstrap.install(this, new BootstrapSettings())
  }

}
