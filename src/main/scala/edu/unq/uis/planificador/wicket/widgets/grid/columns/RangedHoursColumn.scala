package edu.unq.uis.planificador.wicket.widgets.grid.columns

import org.apache.wicket.model.{Model, IModel}
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.repeater.Item
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator
import org.apache.wicket.markup.html.panel.Panel
import edu.unq.uis.planificador.domain.TurnoEmpleado

class RangedHoursColumn(index:Int) extends PropertyColumn[TurnoEmpleado, String](new Model(index.toString), "") {

  override def populateItem(item: Item[ICellPopulator[TurnoEmpleado]], componentId: String, rowModel: IModel[TurnoEmpleado]) {
//    if(rowModel.getObject.horarios.drop(index).head){
      item.add(BlackPanel(componentId))
//    } else {
//      item.add(WhitePanel(componentId))
//    }
  }
}

case class WhitePanel(id:String) extends Panel(id)
case class BlackPanel(id:String) extends Panel(id)