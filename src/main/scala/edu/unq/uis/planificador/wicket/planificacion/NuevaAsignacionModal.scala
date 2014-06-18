package edu.unq.uis.planificador.wicket.planificacion

import de.agilecoders.wicket.core.markup.html.bootstrap.button.{BootstrapAjaxButton, Buttons}
import de.agilecoders.wicket.core.markup.html.bootstrap.dialog.Modal
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior
import edu.unq.uis.planificador.domain.disponibilidad.Turno
import edu.unq.uis.planificador.domain.timeHelpers.TimeInterval
import edu.unq.uis.planificador.domain.{Empleado, Planificacion}
import edu.unq.uis.planificador.ui.empleado.{Hora, NuevaAsignacionModel}
import org.apache.wicket.Component
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator
import org.apache.wicket.extensions.markup.html.repeater.data.table.{DataTable, PropertyColumn}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.{DropDownChoice, Form, FormComponent}
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.markup.repeater.data.ListDataProvider
import org.apache.wicket.model.{CompoundPropertyModel, IModel, Model}

import scala.collection.JavaConversions._
import scala.collection.mutable

class RazonColumn(model: NuevaAsignacionModel) extends PropertyColumn[Empleado, String](new Model("Razon"), "") {
  def getRazonFor(empleado: Empleado) =
    empleado.disponibilidadPara(new Turno(model.fecha, TimeInterval.create(model.inicio.num, model.inicio.num))).disponibilidad.razon

  override def populateItem(item: Item[ICellPopulator[Empleado]], componentId: String, rowModel: IModel[Empleado]) =
    item.add(new Label(componentId, getRazonFor(rowModel.getObject)))
}

class NuevaAsignacionModal(id: String, planificacion: Planificacion)
  extends Modal[NuevaAsignacionModel](id, new CompoundPropertyModel(new NuevaAsignacionModel())) {

  getModelObject.buscador.search
  getModelObject.setFecha(planificacion.fecha)

  setupModalStyle()

  addPanelBusqueda()
  addResultados()

  def addResultados() = {
    add(createResultadosTable)
  }

  def createResultadosTable: Component = {
    def getColumns: mutable.Buffer[PropertyColumn[Empleado, String]] = mutable.Buffer.empty[PropertyColumn[Empleado, String]] :+
      new PropertyColumn[Empleado, String](new Model("Nombre"), "nombreCompleto") :+
      new RazonColumn(getModelObject)

    new DataTable[Empleado, String](
      "resultados", getColumns, new ListDataProvider[Empleado](getModelObject.buscador.empleados), 20
    )
      .add(new TableBehavior().hover())
      .setOutputMarkupId(true)
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
          val modal = NuevaAsignacionModal.this
          modal.getModelObject.buscador.search
          target add createResultadosTable
        }
      }
    )

    add(form)
  }
}
