package edu.unq.uis.planificador.wicket

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.{Model, ResourceModel}
import org.apache.wicket.markup.html.basic.Label
import edu.unq.uis.planificador.wicket.planificacion.detalle.{Planificacion, DetallePage}

class MainPage extends WebPage{
  def helloLabelModel = new ResourceModel("helloWorldLabel")
  def helloLabel = new Label("helloWorldLabel", helloLabelModel)
  add(helloLabel)

  def helloPanel = new DetallePage("detallePanel", Planificacion("Lunes"))
  add(helloPanel)
}