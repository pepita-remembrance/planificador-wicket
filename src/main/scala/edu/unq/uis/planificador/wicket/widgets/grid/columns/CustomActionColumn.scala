package edu.unq.uis.planificador.wicket.widgets.grid.columns

import de.agilecoders.wicket.core.markup.html.bootstrap.button.{BootstrapAjaxLink, Buttons}
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.Broadcast
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.model.{IModel, Model}

class CustomActionColumn[T, S](caption: String, action: (AjaxRequestTarget, IModel[T]) => Unit) extends PropertyColumn[T, S](new Model(""), "") {

  override def populateItem(item: Item[ICellPopulator[T]], componentId: String, rowModel: IModel[T]) {
    item.add(new EditableGridCustomActionPanel[T](componentId, caption, item, action(_, rowModel)) {

      protected def onClick(target: AjaxRequestTarget) {
        action(target, rowModel)
      }
    })
  }
}

class ConditionalCustomActionColumn[T, S](caption: String, action: (AjaxRequestTarget, IModel[T]) => Unit, condition: (T) => Boolean)
  extends CustomActionColumn[T, S](caption, action) {
  override def populateItem(item: Item[ICellPopulator[T]], componentId: String, rowModel: IModel[T]) =
    if (condition(rowModel.getObject))
      super.populateItem(item, componentId, rowModel)
    else
      item.add(new Label(componentId, Model.of("")))
}

abstract class EditableGridCustomActionPanel[T](id: String, caption: String, cellItem: Item[ICellPopulator[T]], action: (AjaxRequestTarget) => Unit) extends Panel(id) {
  val rowItem = cellItem.findParent(classOf[Item[T]])
  rowItem.setOutputMarkupId(true)

  add(newLink(rowItem))

  private def newLink(rowItem: Item[T]) = {
    new BootstrapAjaxLink[String]("link", Buttons.Type.Primary) {
      def onClick(target: AjaxRequestTarget) {
        send(getPage, Broadcast.BREADTH, rowItem)
        target.add(rowItem)
        action(target)
      }
    }
      .setLabel(Model.of(caption))
      .setOutputMarkupId(true)
  }
}