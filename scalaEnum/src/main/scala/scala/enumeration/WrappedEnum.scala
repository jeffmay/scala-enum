package scala.enumeration

import scala.language.implicitConversions

/**
 * A runtime-allocation-free wrapper to get [[Enumeration]] behavior from an [[Enumeration]].
 *
 * @note it is probably better to just declare your enumeration as an [[ValueEnum]], but for the uncommitted,
 *       this acts as an alternative to inheritance.
 */
class WrappedEnum(val enum: scala.Enumeration) extends AnyVal with Enumeration {

  override type ValueType = enum.Value

  override def enumName: String = Enumeration.defaultEnumName(enum)

  override def enumValueTypeName: String = Enumeration.defaultEnumName(enum) + ".Value"

  override implicit def nameOf(value: ValueType): String = value.toString

  override def all: Set[ValueType] = enum.values

  override def findValue(name: String): Option[ValueType] = all.find(nameOf(_) == name)

  override def findValueOrElse(name: String, default: => ValueType): ValueType = findValue(name) getOrElse default

  override def findValueOrThrow(name: String): ValueType = findValue(name) getOrElse {
    throw new MatchError(s"No Value instance with name '$name'")
  }

  override def apply(name: String): ValueType = findValueOrThrow(name)

  override def unapply(name: String): Option[ValueType] = findValue(name)
}

trait ImplicitlyWrapEnum {

  /**
   * Gives normal enumerations [[ValueEnum]]-like behavior implicitly without using inheritance.
   *
   * @note it might be safer / faster to extend [[ValueEnum]], than to depending on the extension
   *       methods to avoid allocation.
   */
  implicit def wrap(enum: scala.Enumeration): Enumeration = new WrappedEnum(enum)
}
