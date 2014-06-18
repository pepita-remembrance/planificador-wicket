package edu.unq.uis.planificador.wicket.planificacion

import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior
import edu.unq.uis.planificador.applicationModel.planificacion.BuscadorPlanificacion
import edu.unq.uis.planificador.domain.TurnoEmpleado
import edu.unq.uis.planificador.wicket.BasePage
import edu.unq.uis.planificador.wicket.widgets.SubPanel
import org.apache.wicket.extensions.markup.html.repeater.data.table.{DataTable, PropertyColumn}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.repeater.data.ListDataProvider
import org.apache.wicket.model.{IModel, CompoundPropertyModel, Model}
import org.wicketstuff.egrid.provider.{EditableListDataProvider, IEditableDataProvider}

import scala.collection.JavaConversions._
import scala.collection.mutable
import edu.unq.uis.planificador.domain.Planificacion
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior
import edu.unq.uis.planificador.wicket.widgets.grid.columns.RangedHoursColumn

class PlanificacionesProvider(planificacion: Planificacion) extends EditableListDataProvider[TurnoEmpleado, String]() {
  override def add(item: TurnoEmpleado) {
    //    planificacion.disponibleLos(item)
  }

  override def remove(item: TurnoEmpleado) {
    //    planificacion.borrarDisponibilidad(item)
  }

  override def getData = planificacion.turnos
}

class HorariosPlanificacionPanel(id: String, planificacion: Planificacion) extends SubPanel(id, planificacion, classOf[TurnoEmpleado]) {
  override def getProvider: IEditableDataProvider[TurnoEmpleado, String] = new PlanificacionesProvider(planificacion)

  override def getColumns: mutable.Buffer[PropertyColumn[TurnoEmpleado, String]] =
    mutable.Buffer.empty[PropertyColumn[TurnoEmpleado, String]] :+
      new PropertyColumn[TurnoEmpleado, String](new Model("Nombre"), "nombre") :+
      new PropertyColumn[TurnoEmpleado, String](new Model("Inicio"), "inicio") :+
      new PropertyColumn[TurnoEmpleado, String](new Model("Fin"), "fin")
}

class TurnosAsignadosPanelSarasa (id: String, planificacionSeleccionada: Planificacion) extends SubPanel(id, planificacionSeleccionada, classOf[TurnoEmpleado]) {
  override def getColumns: mutable.Buffer[PropertyColumn[TurnoEmpleado, String]] = {

    val buffer: mutable.Buffer[PropertyColumn[TurnoEmpleado, String]] = mutable.Buffer.empty[PropertyColumn[TurnoEmpleado, String]]

    buffer += new PropertyColumn[TurnoEmpleado, String](new Model("Nombre"), "nombre")

    List.range(0,24).foreach(index => buffer += new PropertyColumn[TurnoEmpleado, String](new Model(index.toString), s"horarios[$index]"))

    buffer
  }

  override def getProvider: IEditableDataProvider[TurnoEmpleado, String] = new TurnosAsignadosProvider(planificacionSeleccionada)

}

class PlanificacionesPage extends BasePage {
  val ROWS_PER_PAGE = 20

  val buscador = new BuscadorPlanificacion
  val buscarForm = new Form[BuscadorPlanificacion]("buscador", new CompoundPropertyModel[BuscadorPlanificacion](buscador))

  buscador.search

  buscarForm.add(new Label("planificacionSeleccionada.fecha"))

  addResultadosGrid(buscarForm)
  addPlanificacionesPanel(buscarForm)
  addHorariosPanel(buscarForm)
  add(buscarForm)

  def addPlanificacionesPanel(form: Form[BuscadorPlanificacion]) = {
    val panel = new TurnosAsignadosPanelSarasa("turnos", buscador.planificacionSeleccionada)
    panel.setOutputMarkupId(true)

    form.add(panel)
  }

  def addHorariosPanel(form: Form[BuscadorPlanificacion]) = {
    val panel = new HorariosPlanificacionPanel("horarios", buscador.planificacionSeleccionada)
    panel.setOutputMarkupId(true)

    form.add(panel)
  }


  def addTurnosGrid(form: Form[BuscadorPlanificacion]) = {
    val grid = new DataTable[TurnoEmpleado, String](
      "turnos",
      turnosColumns,
      new ListDataProvider[TurnoEmpleado](buscador.planificacionSeleccionada.turnos),
      ROWS_PER_PAGE
    )
      .add(new TableBehavior().hover())
      .setOutputMarkupId(true)

    form.add(grid)
  }

  def turnosColumns = {
    mutable.Buffer.empty[PropertyColumn[TurnoEmpleado, String]] :+
      new PropertyColumn[TurnoEmpleado, String](new Model("Nombre"), "nombre")
  }

  def addResultadosGrid(form: Form[BuscadorPlanificacion]) = {
    val grid = new DataTable[Planificacion, String](
      "grid",
      getColumns,
      new ListDataProvider[Planificacion](buscador.planificaciones),
      ROWS_PER_PAGE
    ){
      override def newRowItem(id: String, index: Int, model: IModel[Planificacion]): Item[Planificacion] = {
        val rowItem: Item[Planificacion] = super.newRowItem(id, index, model)
        rowItem.add(new AjaxFormSubmitBehavior("onclick") {
          override def onEvent(target:AjaxRequestTarget ) {
            buscador.planificacionSeleccionada = model.getObject
            refreshPanels()
            target add buscarForm
          }
        })
        rowItem
      }
    }
      .add(new TableBehavior().hover())
      .setOutputMarkupId(true)

    form.add(grid)
  }

  def getColumns: mutable.Buffer[PropertyColumn[Planificacion, String]] = {
    mutable.Buffer.empty[PropertyColumn[Planificacion, String]] :+
      new PropertyColumn[Planificacion, String](new Model("Fecha"), "fecha") :+
      new PropertyColumn[Planificacion, String](new Model("Estado"), "estado")
  }

  def refreshPanels() = {
    buscarForm.remove("horarios")
    buscarForm.remove("turnos")

    addHorariosPanel(buscarForm)
    addPlanificacionesPanel(buscarForm)
  }
}
