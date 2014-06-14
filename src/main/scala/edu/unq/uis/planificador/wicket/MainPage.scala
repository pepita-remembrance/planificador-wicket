package edu.unq.uis.planificador.wicket

import org.apache.wicket.markup.html.WebPage
import org.apache.wicket.model.{IModel, Model}
import org.wicketstuff.egrid.EditableGrid
import org.wicketstuff.egrid.column.{RequiredEditableTextFieldColumn, AbstractEditablePropertyColumn}
import org.wicketstuff.egrid.provider.EditableListDataProvider
import scala.collection.mutable
import scala.collection.JavaConverters._
import org.apache.wicket.ajax.AjaxRequestTarget
import edu.unq.uis.planificador.domain.builders.RecurrentCalendarSpaceBuilder._
import edu.unq.uis.planificador.domain.calendar.DiaDeSemana.{Martes, Lunes}
import edu.unq.uis.planificador.domain.disponibilidad.Turno
import edu.unq.uis.planificador.homes.{AbstractCollectionBasedHomeEmpleado, EmpleadosCollectionBasedHome}
import edu.unq.uis.planificador.domain.Empleado


class MainPage extends WebPage {
  val empleadoHome: AbstractCollectionBasedHomeEmpleado = EmpleadosCollectionBasedHome

  val pedro = new Empleado("Pedro", "Picapiedras", "123134")
  pedro disponibleLos (Lunes de 14 a 19)
  pedro disponibleLos (Martes de 16 a 20)
  pedro restriccionEl "2014-04-25"
  pedro restriccionEl "2014-04-22"
  pedro asignar (Turno el "2014-04-21" de 14 a 18)
  empleadoHome.create(pedro)

  val juana = new Empleado("juana", "Picapiedras", "8795468")
  juana disponibleLos (Lunes de 9 a 22)
  juana disponibleLos (Martes de 16 a 20)
  empleadoHome.create(juana)

  add(new EditableGrid[Empleado, String](
      "grid",
      getColumns.asJava,
      new EditableListDataProvider[Empleado, String](empleadoHome.allInstances()),
      20,
      classOf[Empleado]
    ) {
      override def displayAddFeature(): Boolean = super.displayAddFeature()

      override def onAdd(target: AjaxRequestTarget, newRow: Empleado): Unit = super.onAdd(target, newRow)

      override def onError(target: AjaxRequestTarget): Unit = super.onError(target)

      override def onSave(target: AjaxRequestTarget, rowModel: IModel[Empleado]): Unit =
        //TODO: Las validaciones del grit para required andan re mal, hay que validar el required de alguna otra manera...
        super.onSave(target, rowModel)

      override def onDelete(target: AjaxRequestTarget, rowModel: IModel[Empleado]): Unit = super.onDelete(target, rowModel)

      override def onCancel(target: AjaxRequestTarget): Unit = super.onCancel(target)
    }
  )

  def getColumns: mutable.Buffer[AbstractEditablePropertyColumn[Empleado, String]] = {
    mutable.Buffer.empty[AbstractEditablePropertyColumn[Empleado, String]] :+
      new RequiredEditableTextFieldColumn[Empleado, String](new Model("Nombre"), "nombre", true) :+
      new RequiredEditableTextFieldColumn[Empleado, String](new Model("Apellido"), "apellido", true) :+
      new RequiredEditableTextFieldColumn[Empleado, String](new Model("Legajo"), "legajo", true)
  }

}
