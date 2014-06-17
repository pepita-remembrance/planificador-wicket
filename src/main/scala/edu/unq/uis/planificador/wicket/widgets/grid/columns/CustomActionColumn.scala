package edu.unq.uis.planificador.wicket.widgets.grid.columns

import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.event.Broadcast
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.model.{Model, IModel}

class CustomActionColumn[T, S](caption: String, action: (AjaxRequestTarget, IModel[T]) => Unit)
  extends PropertyColumn[T, S](new Model(""), "") {

  private final val serialVersionUID: Long = 1L

  override def populateItem(item: Item[ICellPopulator[T]], componentId: String, rowModel: IModel[T]) {
    item.add(new EditableGridCustomActionPanel[T](componentId, caption, item) {
      protected def onClick(target: AjaxRequestTarget) {
        action(target, rowModel)
      }
    })
  }
}

abstract class EditableGridCustomActionPanel[T](id: String, caption: String, cellItem: Item[ICellPopulator[T]]) extends Panel(id) {
  protected def onClick(target: AjaxRequestTarget)

  @SuppressWarnings(Array("unchecked")) val rowItem: Item[T] = cellItem.findParent(classOf[Item[T]])
  add(newLink(rowItem))

  private def newLink(rowItem: Item[T]): AjaxLink[String] = {
    val link = new AjaxLink[String]("link") {
      def onClick(target: AjaxRequestTarget) {
        send(getPage, Broadcast.BREADTH, rowItem)
        target.add(rowItem)
        onClick(target)
      }
    }

    link.add(new Label("label", Model.of(caption)))
    link
  }
}

