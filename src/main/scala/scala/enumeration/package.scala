package scala

import scala.language.implicitConversions

package object enumeration extends ImplicitlyWrapEnum {

  /**
   * Converts [[scala.Enumeration.Value]] instances into the named strings.
   */
  implicit def nameOf(enum: scala.Enumeration#Value): String = enum.toString
}
