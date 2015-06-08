package scala.enumeration

import org.scalatest.WordSpec

import scala.language.implicitConversions

class EnumSpec extends WordSpec {

  "EnumWrapper" should {

    "add extension methods" in {
      assert(NormalEnum.findValueOrThrow(NormalEnum.One) == NormalEnum.One)
    }
  }
  
  "ValueEnum" should {

    "add extension methods" in {
      assert(SimpleValueEnum.findValueOrThrow(SimpleValueEnum.One) == SimpleValueEnum.One)
    }

    "allow prefixes" in {
      PrefixedValueEnum.One startsWith PrefixedValueEnum.Prefix
      assert(PrefixedValueEnum.findValueOrThrow(PrefixedValueEnum.One) == PrefixedValueEnum.One)
    }
  }

  "TypedEnum" should {

    "allow custom enum values" in {
      assert(CustomEnum.findValueOrThrow(CustomEnum.One) == CustomEnum.One)
    }
  }

  "NestedEnum" should {

    "have an expected enumName in different outer class instances" in {
      val outer = new OuterClass
      assert(outer.NestedEnum.enumName == "scala.enumeration.OuterClass.NestedEnum")
    }

    "have an expected enumValueTypeName in different outer class instances" in {
      val outer = new OuterClass
      assert(outer.NestedEnum.enumValueTypeName == "scala.enumeration.OuterClass.NestedEnum.Value")
    }

    "have an expected enumName for a singleton nested num" in {
      assert(OuterObject.OtherNestedEnum.enumName == "scala.enumeration.OuterObject.OtherNestedEnum")
    }

    "have an expected enumValueTypeName for a singleton nested num" in {
      assert(OuterObject.OtherNestedEnum.enumValueTypeName == "scala.enumeration.OuterObject.OtherNestedEnum.Value")
    }
  }

}

object NormalEnum extends scala.Enumeration {
  val One = Value("one")
}

object SimpleValueEnum extends ValueEnum {
  val One = value("one")
}

object PrefixedValueEnum extends ValueEnum {
  val Prefix = "prefix-"
  override protected def value(name: String): ValueType = super.value(Prefix + name)
  val One = value("one")
}

object CustomEnum extends TypedEnum {
  override type ValueType = Custom
  case class Custom(name: String) extends Val(name)
  override lazy val enumValueTypeName: String = defaultEnumValueTypeName
  override protected def value(name: String): ValueType = new Custom(name)
  override implicit def nameOf(value: ValueType): String = value.name
  val One = value("one")
}

class OuterClass {

  object NestedEnum extends scala.Enumeration {
    val One = Value("one")
  }
}

object OuterObject extends OuterClass {

  object OtherNestedEnum extends scala.Enumeration {
    val One = Value("one")
  }
}
