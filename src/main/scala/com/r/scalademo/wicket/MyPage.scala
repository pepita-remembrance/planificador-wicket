package com.r.scalademo.wicket

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.{Model, ResourceModel}

class MyPage(parameters :PageParameters) extends WebPage{
  def helloLabelModel = new ResourceModel("helloWorldLabel")
  def helloLabel = new Label("helloWorldLabel", helloLabelModel)
  add(helloLabel)

  def helloPanel = new MyHelloPanel("helloPanel", new Model())
  add(helloPanel)
}
