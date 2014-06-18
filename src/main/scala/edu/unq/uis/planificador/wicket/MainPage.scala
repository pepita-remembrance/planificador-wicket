package edu.unq.uis.planificador.wicket

import edu.unq.uis.planificador.applicationModel.empleado.BuscadorEmpleados
import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.wicket.empleado.{DisponibilidadEmpleadoPanel, RestriccionesEmpleadoPanel}
import edu.unq.uis.planificador.wicket.widgets.grid.BootstrapEditableGrid
import edu.unq.uis.planificador.wicket.widgets.grid.columns.CustomActionColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.{CompoundPropertyModel, Model}
import org.wicketstuff.egrid.column.RequiredEditableTextFieldColumn
import org.wicketstuff.egrid.provider.EditableListDataProvider

import scala.collection.JavaConversions._
import scala.collection.mutable

class MainPage extends WebPage {
  val ROWS_PER_PAGE = 20

  val buscador = new BuscadorEmpleados()
  val buscarForm = new Form[BuscadorEmpleados]("buscadorEmpleados", new CompoundPropertyModel[BuscadorEmpleados](buscador))

  buscador.search

  addResultadosGrid(buscarForm)

  buscarForm.add(new Label("empleadoSeleccionado.nombreCompleto"))
  addDisponibilidadesPanel(buscarForm)
  addRestriccionesPanel(buscarForm)

  add(buscarForm)

  def addDisponibilidadesPanel(form: Form[BuscadorEmpleados]) = {
    val panel = new DisponibilidadEmpleadoPanel("disponibilidades", buscador.empleadoSeleccionado)
    panel.setOutputMarkupId(true)

    form.add(panel)
  }

  def addRestriccionesPanel(form: Form[BuscadorEmpleados]) = {
    val panel = new RestriccionesEmpleadoPanel("restricciones", buscador.empleadoSeleccionado)
    panel.setOutputMarkupId(true)

    form.add(panel)
  }  
  
  def addResultadosGrid(form: Form[BuscadorEmpleados]) = {
    val grid = new BootstrapEditableGrid[Empleado, String](
      "grid",
      getColumns,
      new EditableListDataProvider[Empleado, String](buscador.empleados),
      ROWS_PER_PAGE,
      classOf[Empleado]
    )

    form.add(grid)
  }

  def getColumns: mutable.Buffer[PropertyColumn[Empleado, String]] = {
    mutable.Buffer.empty[PropertyColumn[Empleado, String]] :+
      new RequiredEditableTextFieldColumn[Empleado, String](new Model("Nombre"), "nombre", true) :+
      new RequiredEditableTextFieldColumn[Empleado, String](new Model("Apellido"), "apellido", true) :+
      new RequiredEditableTextFieldColumn[Empleado, String](new Model("Legajo"), "legajo", true) :+
      new CustomActionColumn[Empleado, String](
        "Ver detalle",
        (target, model) => {
          buscador.empleadoSeleccionado = model.getObject

          //Tengo que hacer esto a mano porque el Grid no se banca un Model
          refreshPanels()

          target add buscarForm
        }
      )
  }

  def refreshPanels() {
    buscarForm.remove("disponibilidades")
    addDisponibilidadesPanel(buscarForm)

    buscarForm.remove("restricciones")
    addRestriccionesPanel(buscarForm)
  }
}