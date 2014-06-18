package edu.unq.uis.planificador.wicket.empleado

import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.wicket.widgets.grid.BootstrapEditableGrid
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.{IModel, Model}
import org.wicketstuff.egrid.provider.IEditableDataProvider

import scala.collection.JavaConversions._
import scala.collection.mutable

abstract class EmpleadosSubPanel[A](id: String, val empleadoSeleccionado: Empleado, clazz: Class[A]) extends Panel(id, new Model(empleadoSeleccionado)) {
  val ROWS_PER_PAGE = 20

  add(createGrid)

  def createGrid = new BootstrapEditableGrid[A, String]("grid", getColumns, getProvider, ROWS_PER_PAGE, clazz) {
    override def onDelete(target: AjaxRequestTarget, rowModel: IModel[A]) = {
      getProvider.remove(rowModel.getObject)
      refreshGrid(target)
    }

    override def onAdd(target: AjaxRequestTarget, newRow: A) {
      refreshGrid(target)
    }
  }

  def getProvider: IEditableDataProvider[A, String]

  def getColumns: mutable.Buffer[PropertyColumn[A, String]]

  def refreshGrid(target: AjaxRequestTarget) {
    removeAll()
    add(createGrid)

    target add this
  }
}
