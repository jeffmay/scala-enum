package scala.enumeration

import org.scalacheck.{Arbitrary, Gen}
import play.api.libs.json.{JsError, JsString, Json}
import play.api.libs.json.scalatest.PlayJsonFormatSpec

object JsonExampleEnum extends SerializableValueEnum {

  val N = Value(1)
  val X = value("X")
  val Y = value("Y")

  implicit val arbValue: Arbitrary[ValueType] = Arbitrary(Gen.oneOf(all.toSeq))
}

object OuterObject {

  object NestedJsonExampleEnum extends SerializableValueEnum {

    val N = Value(1)
    val X = value("X")
    val Y = value("Y")

    implicit val arbValue: Arbitrary[ValueType] = Arbitrary(Gen.oneOf(all.toSeq))
  }
}

import scala.enumeration.OuterObject.NestedJsonExampleEnum._
import scala.enumeration.JsonExampleEnum._

class ValueJsonSerializationSpec extends PlayJsonFormatSpec[JsonExampleEnum.ValueType] {

  it should "return a JsError when deserializing an unknown value" in {
    val result = Json.fromJson[JsonExampleEnum.ValueType](JsString("unknown"))
    result match {
      case JsError(Seq((_, Seq(error)))) =>
        assert(error.message startsWith "error.expected.enum.value")
      case _ =>
        fail(s"Unexpected JsResult: $result")
    }
  }
}

class NestedValueJsonSerializationSpec extends PlayJsonFormatSpec[OuterObject.NestedJsonExampleEnum.ValueType]

