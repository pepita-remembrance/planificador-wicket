package edu.unq.uis.planificador.wicket.widgets.grid.columns

import org.apache.wicket.model.IModel
import org.wicketstuff.egrid.column.{AbstractEditablePropertyColumn, EditableCellPanel, EditableRequiredDropDownCellPanel}

import scala.collection.JavaConversions._

class RequiredEditableDropDownColumn[T, S](model: IModel[String], propertyExpression: String, values: Iterable[Any]) extends AbstractEditablePropertyColumn[T, S](model, propertyExpression) {
  override def getEditableCellPanel(componentId: String): EditableCellPanel = {
    new EditableRequiredDropDownCellPanel[T, S](componentId, this, values.toList)
  }
}
