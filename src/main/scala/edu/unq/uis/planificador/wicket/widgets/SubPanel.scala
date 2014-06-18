package edu.unq.uis.planificador.wicket.widgets

import edu.unq.uis.planificador.wicket.widgets.grid.BootstrapEditableGrid
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.{IModel, Model}
import org.wicketstuff.egrid.provider.IEditableDataProvider

import scala.collection.JavaConversions._
import scala.collection.mutable

abstract class SubPanel[Entidad <: java.io.Serializable, ListElement](id: String, val entidadSeleccionada: Entidad, clazz: Class[ListElement]) extends Panel(id, new Model(entidadSeleccionada)) {
  val ROWS_PER_PAGE = 20

  add(createGrid)

  def createGrid = new BootstrapEditableGrid[ListElement, String]("grid", getColumns, getProvider, ROWS_PER_PAGE, clazz) {
    override def onDelete(target: AjaxRequestTarget, rowModel: IModel[ListElement]) = {
      getProvider.remove(rowModel.getObject)
      refreshGrid(target)
    }

    override def onAdd(target: AjaxRequestTarget, newRow: ListElement) {
      refreshGrid(target)
    }
  }

  def getProvider: IEditableDataProvider[ListElement, String]

  def getColumns: mutable.Buffer[PropertyColumn[ListElement, String]]

  def refreshGrid(target: AjaxRequestTarget) {
    removeAll()
    add(createGrid)

    target add this
  }
}
