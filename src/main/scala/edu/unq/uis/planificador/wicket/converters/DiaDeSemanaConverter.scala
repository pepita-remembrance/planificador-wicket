package edu.unq.uis.planificador.wicket.converters

import java.util.Locale

import edu.unq.uis.planificador.domain.calendar.DiaDeSemana
import org.apache.wicket.util.convert.IConverter

class DiaDeSemanaConverter extends IConverter[DiaDeSemana] {
  override def convertToObject(p1: String, p2: Locale): DiaDeSemana = DiaDeSemana.todos.find(_.nombre == p1).get

  override def convertToString(p1: DiaDeSemana, p2: Locale): String = p1.nombre
}
