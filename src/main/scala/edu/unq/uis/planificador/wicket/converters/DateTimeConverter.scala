package edu.unq.uis.planificador.wicket.converters

import java.util.Locale

import edu.unq.uis.planificador.domain.calendar.DiaDeSemana
import org.apache.wicket.util.convert.IConverter
import org.joda.time.DateTime

class DateTimeConverter extends IConverter[DateTime] {
  override def convertToObject(p1: String, p2: Locale): DateTime = new DateTime(p1)

  override def convertToString(p1: DateTime, p2: Locale): String = s"${DiaDeSemana.fromFecha(p1).nombre} ${p1.toString("dd/MM")}"
}
