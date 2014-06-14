package edu.unq.uis.planificador.wicket

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.{IModel, Model, ResourceModel}
import org.apache.wicket.markup.html.basic.Label
import edu.unq.uis.planificador.wicket.planificacion.detalle.DetallePage
import org.wicketstuff.egrid.EditableGrid
import org.wicketstuff.egrid.column.{EditableTextFieldPropertyColumn, AbstractEditablePropertyColumn, RequiredEditableTextFieldColumn}
import org.wicketstuff.egrid.provider.EditableListDataProvider
import scala.collection.mutable
import scala.collection.JavaConverters._
import edu.unq.uis.planificador.wicket.planificacion.detalle.Planificacion
import org.apache.wicket.ajax.AjaxRequestTarget

class MainPage extends WebPage {
  def helloLabelModel = new ResourceModel("helloWorldLabel")

  def helloLabel = new Label("helloWorldLabel", helloLabelModel)

  add(helloLabel)

  def helloPanel = new DetallePage("detallePanel", Planificacion("Lunes"))

  add(helloPanel)
  add(new EditableGrid[Persona, String](
    "grid",
    getColumns.asJava,
    new EditableListDataProvider[Persona, String](mutable.Buffer(Persona.llamada("Juan"), Persona.llamada("Marta")).asJava),
    20,
    classOf[Persona]) {
    override def displayAddFeature(): Boolean = super.displayAddFeature()

    override def onAdd(target: AjaxRequestTarget, newRow: Persona): Unit = super.onAdd(target, newRow)

    override def onError(target: AjaxRequestTarget): Unit = super.onError(target)

    override def onSave(target: AjaxRequestTarget, rowModel: IModel[Persona]): Unit =
      super.onSave(target, rowModel)

    override def onDelete(target: AjaxRequestTarget, rowModel: IModel[Persona]): Unit = super.onDelete(target, rowModel)

    override def onCancel(target: AjaxRequestTarget): Unit = super.onCancel(target)
  }
  )

  def getColumns: mutable.Buffer[AbstractEditablePropertyColumn[Persona, String]] = {
    mutable.Buffer.empty[AbstractEditablePropertyColumn[Persona, String]] :+
      new EditableTextFieldPropertyColumn[Persona, String](new Model("Nombre"), "nombre", true)
  }

  var l = new EditableListDataProvider[Persona, String](mutable.Buffer(Persona.llamada("Juan"), Persona.llamada("Marta")).asJava)
}

class Persona extends Serializable {
  var nombre: String = ""
}

object Persona {
  def llamada(nombre: String): Persona = {
    val persona: Persona = new Persona()
    persona.nombre = nombre
    persona
  }
}