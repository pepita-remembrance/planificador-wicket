package edu.unq.uis.planificador.wicket.planificacion

import de.agilecoders.wicket.core.markup.html.bootstrap.button.{BootstrapAjaxButton, Buttons}
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior
import edu.unq.uis.planificador.domain.disponibilidad.{Restriccion, Turno}
import edu.unq.uis.planificador.domain.timeHelpers.TimeInterval
import edu.unq.uis.planificador.domain.{Empleado, Planificacion}
import edu.unq.uis.planificador.ui.empleado.{Hora, NuevaAsignacionModel}
import edu.unq.uis.planificador.wicket.widgets.grid.columns.ConditionalCustomActionColumn
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
    empleado.disponibilidadPara(new Turno(model.fecha, TimeInterval.create(model.inicio.num, model.fin.num))).disponibilidad.razon

  override def populateItem(item: Item[ICellPopulator[Empleado]], componentId: String, rowModel: IModel[Empleado]) =
    item.add(new Label(componentId, getRazonFor(rowModel.getObject)))
}

class NuevaAsignacionForm(id: String, planificacion: Planificacion)
  extends Form[NuevaAsignacionModel](id, new CompoundPropertyModel(new NuevaAsignacionModel())) {

  getModelObject.buscador.search
  getModelObject.setFecha(planificacion.fecha)

  addPanelBusqueda()
  addResultados()

  def addResultados() = {
    add(createResultadosTable)
  }

  def createResultadosTable: Component = {
    def getColumns: mutable.Buffer[PropertyColumn[Empleado, String]] = mutable.Buffer.empty[PropertyColumn[Empleado, String]] :+
      new PropertyColumn[Empleado, String](new Model("Nombre"), "nombreCompleto") :+
      new RazonColumn(getModelObject) :+
      new ConditionalCustomActionColumn[Empleado, String]("Asignar", (target, model) => {
        model.getObject.asignar(
          Turno el planificacion.fecha de getModelObject.inicio.num a getModelObject.fin.num
        )

        target add this
      }, this.puedeSerAsignado)

    new DataTable[Empleado, String](
      "resultados", getColumns, new ListDataProvider[Empleado](getModelObject.buscador.empleados), 20
    )
      .add(new TableBehavior().hover())
      .setOutputMarkupId(true)
  }

  def puedeSerAsignado(model: Empleado) = {
    val interval = TimeInterval.create(getModelObject.inicio.num, getModelObject.fin.num)
    val cantidadDeHoras = interval.toDuration.getStandardHours

    val turno = new Turno(planificacion.fecha, interval)
    cantidadDeHoras >= 4 && cantidadDeHoras <= 8 && model.disponibilidadPara(turno).disponibilidad != Restriccion
  }

  def addPanelBusqueda() = {
    val form = new BootstrapForm[NuevaAsignacionModel]("busquedaForm", this.getModel)

    def createField(name: String): FormComponent[Hora] = new DropDownChoice[Hora](name, getModelObject.turnos)

    form.add(
      createField("inicio"),
      createField("fin"),

      new BootstrapAjaxButton("buscar", Buttons.Type.Success) {
        override def onSubmit(target: AjaxRequestTarget, form: Form[_]) = {
          val modal = NuevaAsignacionForm.this
          modal.getModelObject.buscador.search
          target add modal
        }
      }
    )

    add(form)
  }
}
