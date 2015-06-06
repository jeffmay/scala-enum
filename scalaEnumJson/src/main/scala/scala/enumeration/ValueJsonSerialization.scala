package scala.enumeration

import play.api.libs.json._

/**
 * A wrapper around [[scala.Enumeration]] that gives you the helpful [[Enumeration]] interface
 * with the additional benefit of having an implicit Play Json [[Format]] for your enum values.
 */
abstract class SerializableValueEnum extends ValueEnum with ValueJsonSerialization

/**
 * Adds an implicit Play Json [[Format]] for this Enumeration's ValueType.
 *
 * @note this is a trait to allow mixin composition with other [[ValueEnum]] subclasses.
 */
trait ValueJsonSerialization {
  self: ValueEnum =>

  object FormatAsString extends Format[ValueType] {
    override def reads(json: JsValue): JsResult[ValueType] = {
      json.validate[String] flatMap { name =>
        findValue(name) match {
          case Some(value) => JsSuccess(value)
          case None => JsError(s"error.expected.enum.value: unrecognized value '$name' in $enumName")
        }
      }
    }
    override def writes(value: ValueType): JsValue = JsString(value.toString)
  }

  implicit def format: Format[ValueType] = FormatAsString
}
