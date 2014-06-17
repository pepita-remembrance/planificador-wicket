package edu.unq.uis.planificador.wicket.empleado

import edu.unq.uis.planificador.domain.Empleado
import edu.unq.uis.planificador.domain.calendar.RecurrentCalendarSpace
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.{IModel, Model}
import org.wicketstuff.egrid.EditableGrid
import org.wicketstuff.egrid.column.RequiredEditableTextFieldColumn
import org.wicketstuff.egrid.provider.EditableListDataProvider

import scala.collection.JavaConverters._
import scala.collection.mutable

class DisponibilidadEmpleadoPanel(var empleadoSeleccionado: Empleado) extends Panel("disponibilidades", new Model(empleadoSeleccionado)) {
  val ROWS_PER_PAGE = 20

  add(new EditableGrid[RecurrentCalendarSpace, String](
    "grid",
    getColumns.asJava,
    new EditableListDataProvider[RecurrentCalendarSpace, String](empleadoSeleccionado.disponibilidades.asJava),
    ROWS_PER_PAGE,
    classOf[RecurrentCalendarSpace]
  ) {
    override def displayAddFeature(): Boolean = super.displayAddFeature()

    override def onAdd(target: AjaxRequestTarget, newRow: RecurrentCalendarSpace): Unit = super.onAdd(target, newRow)

    override def onError(target: AjaxRequestTarget): Unit = super.onError(target)

    override def onSave(target: AjaxRequestTarget, rowModel: IModel[RecurrentCalendarSpace]): Unit =
    //TODO: Las validaciones del grit para required andan re mal, hay que validar el required de alguna otra manera...
      super.onSave(target, rowModel)

    override def onDelete(target: AjaxRequestTarget, rowModel: IModel[RecurrentCalendarSpace]): Unit = super.onDelete(target, rowModel)

    override def onCancel(target: AjaxRequestTarget): Unit = super.onCancel(target)
  }
  )

  def setModel(model: IModel[Empleado]) {
    empleadoSeleccionado = model.getObject
  }

  def getColumns: mutable.Buffer[PropertyColumn[RecurrentCalendarSpace, String]] = {
    mutable.Buffer.empty[PropertyColumn[RecurrentCalendarSpace, String]] :+
      new RequiredEditableTextFieldColumn[RecurrentCalendarSpace, String](new Model("Dia"), "diaDeSemana", true) :+
      new RequiredEditableTextFieldColumn[RecurrentCalendarSpace, String](new Model("Inicio"), "inicio", true) :+
      new RequiredEditableTextFieldColumn[RecurrentCalendarSpace, String](new Model("Fin"), "fin", true)
  }
}