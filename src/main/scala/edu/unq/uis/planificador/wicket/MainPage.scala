package edu.unq.uis.planificador.wicket.main

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.{Model, ResourceModel}
import org.apache.wicket.markup.html.basic.Label
import edu.unq.uis.planificador.wicket.planificacion.detalle.DetallePage

class MainPage extends WebPage{
  def helloLabelModel = new ResourceModel("helloWorldLabel")
  def helloLabel = new Label("helloWorldLabel", helloLabelModel)
  add(helloLabel)
//
//  def helloPanel = new DetallePage("helloPanel", new Model())
//  add(helloPanel)
}