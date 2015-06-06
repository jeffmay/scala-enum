package scala.enumeration

import scala.language.implicitConversions
import scala.reflect.ClassTag

/**
 * An implementation of [[Enumeration]] that
 *
 * This method uses the unadorned [[scala.Enumeration.Value]] type. If you would like to extend this type
 * instead, you can use [[TypedEnum]].
 */
abstract class ValueEnum extends AbstractEnumeration {

  final override type ValueType = Value

  // final since Value's name is the only safe way to get the unique name
  @inline final override implicit def nameOf(value: ValueType): String = value.toString

  override def all: Set[ValueType] = values

  override def enumValueTypeName: String = "Value"

  override protected def value(name: String): ValueType = Value(name)
}

/**
 * An Enumeration that returns a subclass of the [[scala.Enumeration.Value]] inner class
 * from all of its helper methods.
 *
 * You must define the [[Enumeration.ValueType]] on the subclass.
 */
abstract class TypedEnum extends AbstractEnumeration {

  override type ValueType <: Val

  /**
   * The name of the [[ValueType]] class for error messaging.
   *
   * A good default implementation is [[defaultEnumValueTypeName]]
   */
  override def enumValueTypeName: String

  /**
   * A helper method for setting [[enumValueTypeName]].
   */
  protected def defaultEnumValueTypeName(implicit tag: ClassTag[ValueType]): String = {
    val full = tag.runtimeClass.getName
    full.substring(full.lastIndexOf("$"), full.length)
  }

  /**
   * A method to allow the enumeration to override the default method
   * of casting values to the expected [[scala.Enumeration.Value]].
   *
   * @param value A value as added to this Enumeration's [[scala.Enumeration.ValueSet]]
   * @return the value with the correct type
   */
  implicit def convertToValueType(value: Value): ValueType = value.asInstanceOf[ValueType]

  /**
   * Gets all the enumerated values as the correct type.
   */
  override def all: Set[ValueType] = values.toSet map convertToValueType
}

private[enumeration] abstract class AbstractEnumeration extends scala.Enumeration with Enumeration {

  private[this] lazy val lookup: Map[String, ValueType] = all.map(v => (nameOf(v), v)).toMap

  override lazy val enumName: String = Enumeration.defaultEnumName(this)

  protected def value(name: String): ValueType

  @inline final override def findValue(name: String): Option[ValueType] = lookup get name

  @inline final override def findValueOrElse(name: String, default: => ValueType): ValueType =
    lookup.getOrElse(name, default)

  // Not final to allow overriding the error message
  override def findValueOrThrow(name: String): ValueType =
    findValueOrElse(name, throw new MatchError(s"No $enumValueTypeName instance with name '$name'"))

  override def unapply(name: String): Option[ValueType] = findValue(name)

  override def apply(name: String): ValueType = findValueOrThrow(name)
}



