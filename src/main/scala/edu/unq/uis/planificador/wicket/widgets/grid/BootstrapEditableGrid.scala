package edu.unq.uis.planificador.wicket.widgets.grid

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn
import org.wicketstuff.egrid.EditableGrid
import org.wicketstuff.egrid.provider.IEditableDataProvider

class BootstrapEditableGrid[T, S](id: String, columns: java.util.List[_ <: IColumn[T, S]],
                                  dataProvider: IEditableDataProvider[T, S], rowsPerPage: Long, clazz: Class[T]) extends EditableGrid[T, S](id, columns, dataProvider, rowsPerPage, clazz) {
}
