package edu.unq.uis.planificador.wicket.converters

import java.util.Locale

import edu.unq.uis.planificador.ui.empleado.Hora
import org.apache.wicket.util.convert.IConverter

class HoraConverter extends IConverter[Hora] {
  override def convertToObject(p1: String, p2: Locale): Hora = new Hora(p1.toInt)

  override def convertToString(p1: Hora, p2: Locale): String = p1.readable
}