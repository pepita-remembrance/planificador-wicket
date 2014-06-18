package edu.unq.uis.planificador.wicket.planificacion

import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior
import edu.unq.uis.planificador.applicationModel.planificacion.BuscadorPlanificacion
import edu.unq.uis.planificador.domain.Planificacion
import edu.unq.uis.planificador.wicket.BasePage
import edu.unq.uis.planificador.wicket.widgets.grid.columns.CustomActionColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.{DataTable, PropertyColumn}
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.repeater.data.ListDataProvider
import org.apache.wicket.model.{CompoundPropertyModel, Model}

import scala.collection.JavaConversions._
import scala.collection.mutable

class PlanificacionesPage extends BasePage {
  val ROWS_PER_PAGE = 20

  val buscador = new BuscadorPlanificacion
  val buscarForm = new Form[BuscadorPlanificacion]("buscador", new CompoundPropertyModel[BuscadorPlanificacion](buscador))

  buscador.search

  addResultadosGrid(buscarForm)
  add(buscarForm)

  def addResultadosGrid(form: Form[BuscadorPlanificacion]) = {
    val grid = new DataTable[Planificacion, String](
      "grid",
      getColumns,
      new ListDataProvider[Planificacion](buscador.planificaciones),
      ROWS_PER_PAGE
    ).add(new TableBehavior().hover())

    form.add(grid)
  }

  def getColumns: mutable.Buffer[PropertyColumn[Planificacion, String]] = {
    mutable.Buffer.empty[PropertyColumn[Planificacion, String]] :+
      new PropertyColumn[Planificacion, String](new Model("Fecha"), "fecha") :+
      new PropertyColumn[Planificacion, String](new Model("Estado"), "estado") :+
      new CustomActionColumn[Planificacion, String](
        "Ver detalle",
        (target, model) => {
          buscador.planificacionSeleccionada = model.getObject

          //Tengo que hacer esto a mano porque el Grid no se banca un Model
          refreshPanels()

          target add buscarForm
        }
      )
  }

  def refreshPanels() = {}
}
