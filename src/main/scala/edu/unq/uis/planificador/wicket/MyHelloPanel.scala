package com.r.scalademo.wicket

import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.{Model, IModel}
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior

class MyHelloPanel(id: String, model: IModel[_]) extends Panel(id, model) {

  // One more constructor
  def this(id: String) = this(id, new Model)

  // Input filed
  val inputField = new TextField[String]("inputField", Model.of(""))
  add(inputField)

  // Hello label
  val labelModel = new Model[String]
  val label = new Label("helloLabel", labelModel)
  label.setOutputMarkupId(true)
  add(label)

  // Doing update on the fly with ajax
  val ajaxBehavior = new OnChangeAjaxBehavior {
    def onUpdate(target: AjaxRequestTarget) {
      // Ugly implementation. StringResourceModel must be used instead.
      labelModel.setObject("Hello " + inputField.getInput + "!")
      target.add(label)
    }
  }
  inputField.add(ajaxBehavior)
}

