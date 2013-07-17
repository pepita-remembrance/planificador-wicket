package com.r.scalademo.wicket

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.ResourceModel

class MyPage(parameters :PageParameters) extends WebPage{
  def helloLabelModel = new ResourceModel("helloWorldLabel")
  def helloLabel = new Label("helloWorldLabel", helloLabelModel)
  add(helloLabel)
}
