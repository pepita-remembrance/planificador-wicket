package edu.unq.uis.planificador.wicket

import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.homes.{AbstractCollectionBasedHomeEmpleado, EmpleadosCollectionBasedHome}
import edu.unq.uis.planificador.wicket.empleado.DisponibilidadEmpleadoPanel
import edu.unq.uis.planificador.wicket.widgets.grid.columns.CustomActionColumn
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.{IModel, Model}
import org.wicketstuff.egrid.EditableGrid
import org.wicketstuff.egrid.column.RequiredEditableTextFieldColumn
import org.wicketstuff.egrid.provider.EditableListDataProvider

import scala.collection.JavaConverters._
import scala.collection.mutable

class MainPage extends WebPage {
  val ROWS_PER_PAGE = 20

  val empleadoHome: AbstractCollectionBasedHomeEmpleado = EmpleadosCollectionBasedHome
  var disponibilidadEmpleadoPanel: DisponibilidadEmpleadoPanel = _

  add(new EditableGrid[Empleado, String](
    "grid",
    getColumns.asJava,
    new EditableListDataProvider[Empleado, String](empleadoHome.allInstances),
    ROWS_PER_PAGE,
    classOf[Empleado]
  ) {
    override def onAdd(target: AjaxRequestTarget, newRow: Empleado): Unit = empleadoHome.create(newRow)

    override def onDelete(target: AjaxRequestTarget, rowModel: IModel[Empleado]): Unit = empleadoHome.delete(rowModel.getObject)
  }
  )

  setDisponibilidadesPanel(empleadoHome.allInstances.get(0))
  add(disponibilidadEmpleadoPanel)

  def getColumns: mutable.Buffer[PropertyColumn[Empleado, String]] = {
    mutable.Buffer.empty[PropertyColumn[Empleado, String]] :+
      new RequiredEditableTextFieldColumn[Empleado, String](new Model("Nombre"), "nombre", true) :+
      new RequiredEditableTextFieldColumn[Empleado, String](new Model("Apellido"), "apellido", true) :+
      new RequiredEditableTextFieldColumn[Empleado, String](new Model("Legajo"), "legajo", true) :+
      new CustomActionColumn[Empleado, String](
        "Ver disponibilidades",
        (target, model) => {
          val currentPanel = disponibilidadEmpleadoPanel
          setDisponibilidadesPanel(model.getObject)

          currentPanel.replaceWith(disponibilidadEmpleadoPanel)

          target.add(disponibilidadEmpleadoPanel)
        }
      )
  }

  def setDisponibilidadesPanel(empleado: Empleado) = {
    disponibilidadEmpleadoPanel = new DisponibilidadEmpleadoPanel(empleado)
    disponibilidadEmpleadoPanel.setOutputMarkupId(true)
  }
}