package scala.enumeration

import scala.language.implicitConversions

/**
 * A basic Enumeration interface that is more Scala friendly than [[scala.Enumeration]], but adds the
 * additional requirement that there always be a unique string name for lookup purposes.
 *
 * Designed to work with [[scala.Enumeration]], this trait provides a common type alias for reference
 * in subclasses within the enumeration type hierarchy so that the final result is always the same type.
 */
trait Enumeration extends Any {

  /**
   * Override this type alias to allow returning more specific [[ValueType]] type
   * subclass from these Enumeration helper methods.
   */
  type ValueType

  /**
   * Gets all the enumerated values as the correct type.
   */
  def all: Set[ValueType]

  /**
   * A helpful name of this Enumeration object for debugging. Typically this is just the class name.
   *
   * @note this is not intended to be a unique identifier, since simple names might also be desirable here.
   */
  def enumName: String

  /**
   * A helpful name of the [[ValueType]] class for debugging. Typically this is either a full class name
   * or a short name relative to this outer [[Enumeration]] object.
   *
   * @note this is not intended to be a unique identifier, since simple names might also be desirable here.
   */
  def enumValueTypeName: String

  /**
   * Gets the unique name of the enumeration value.
   */
  implicit def nameOf(value: ValueType): String

  /**
   * Find the Enum Value associated with this string
   * @param name the unique name of the enumeration value
   * @return An Option containing the associated value if it exists
   */
  def findValue(name: String): Option[ValueType]

  /**
   * Find the Enum Value associated with this string or return a default
   * @param name the unique name of the enumeration value
   * @param default A default value to return if the string is not found
   * @return The associated value if it is found, otherwise the default
   */
  def findValueOrElse(name: String, default: => ValueType): ValueType

  /**
   * Find the Enum Value associated with this string or throw a helpful exception
   * @param name the unique name of the enumeration value
   * @return The associated value
   * @throws MatchError if the unique name does not exist in the enumeration value set
   */
  def findValueOrThrow(name: String): ValueType

  /**
   * Unapply method to allow for easy extraction / case class comparison
   * @param name the unique name of the enumeration value
   * @return An Option containing the associated value if it exists
   */
  def unapply(name: String): Option[ValueType]

  /**
   * Apply method to allow for easy creation of enum values
   * @param name the unique name of the enumeration value
   * @return the value of this Enum with the matching name
   * @throws MatchError if the unique name does not exist in the enumeration value set
   */
  def apply(name: String): ValueType
}

object Enumeration extends ImplicitlyWrapEnum {

  // A helper method for enumeration names
  @inline private[enumeration] def defaultEnumName(enum: scala.Enumeration): String = {
    val className = enum.getClass.getName
    // and replace all '$'s with '.'s
    val dottedName = className.replaceAll("""(\.type)*\$""", ".")
    // remove the trailing $
    dottedName.substring(0, dottedName.length - 1)
  }
}
