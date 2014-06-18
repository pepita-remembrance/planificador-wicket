package edu.unq.uis.planificador.wicket.planificacion

import de.agilecoders.wicket.core.markup.html.bootstrap.button.{BootstrapAjaxButton, Buttons}
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm
import edu.unq.uis.planificador.domain.Planificacion
import edu.unq.uis.planificador.ui.empleado.{Hora, NuevaAsignacionModel}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.form.{DropDownChoice, Form, FormComponent}
import org.apache.wicket.model.{CompoundPropertyModel, Model}

import scala.collection.JavaConversions._

class NuevaAsignacionModal(id: String, planificacion: Planificacion)
  extends Modal[NuevaAsignacionModel](id, new CompoundPropertyModel(new NuevaAsignacionModel())) {

  getModelObject.buscador.search
  getModelObject.setFecha(planificacion.fecha)

  setupModalStyle()

  addPanelBusqueda()
  addResultados()

  def addResultados() = {

  }

  def setupModalStyle() = {
    addCloseButton()
    header(Model.of("Nueva asignación"))
    setUseCloseHandler(true)
    show(false)
    size(Modal.Size.Medium)
    setUseKeyboard(true)
  }

  def addPanelBusqueda() = {
    val form = new BootstrapForm[NuevaAsignacionModel]("busquedaForm", this.getModel)

    def createField(name: String): FormComponent[Hora] = new DropDownChoice[Hora](name, getModelObject.turnos)

    form.add(
      createField("inicio"),
      createField("fin"),

      new BootstrapAjaxButton("buscar", Buttons.Type.Success) {
        override def onSubmit(target: AjaxRequestTarget, form: Form[_]) = {
          NuevaAsignacionModal.this.getModelObject.buscador.search
          target add form
        }
      }
    )

    add(form)
  }
}
