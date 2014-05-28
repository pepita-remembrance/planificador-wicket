package edu.unq.uis.planificador.wicket.planificacion.detalle

import org.apache.wicket.model.{PropertyModel, CompoundPropertyModel, Model, IModel}
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.html.form.TextField

class DetallePage(id: String, planificacion: Planificacion)
  extends Panel(id, new Model[Planificacion](planificacion)) {

  add(
    new TextField[String]("diaDeSemana", new PropertyModel[String](getDefaultModel, "diaDeSemana"))
  )
}

case class Planificacion(var diaDeSemana:String)