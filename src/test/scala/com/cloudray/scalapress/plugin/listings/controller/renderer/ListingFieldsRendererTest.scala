package com.cloudray.scalapress.plugin.listings.controller.renderer

import com.cloudray.scalapress.item.attr.{AttributeType, AttributeOption, Attribute}
import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class ListingFieldsRendererTest extends FlatSpec with OneInstancePerTest with MockitoSugar {

  val a1 = new Attribute
  a1.id = 1
  a1.name = "team"
  a1.multipleValues = true
  a1.attributeType = AttributeType.Selection

  a1.options.add(AttributeOption(10, a1, "boro", 1))
  a1.options.add(AttributeOption(11, a1, "sunderland", 2))
  a1.options.add(AttributeOption(12, a1, "toon", 3))
  a1.options.add(AttributeOption(13, a1, "hartlepool", 4))


  "a fields renderer" should "use checkboxes for multiple selections" in {
    a1.multipleValues = true
    val output = ListingFieldsRenderer._attributeInputs(Seq(a1), Nil)
    println(output)
    assert(output.mkString.contains("<input type=\"checkbox\""))
    assert(!output.mkString.contains("<select"))
  }

  "a fields renderer" should "use a select for singluar selections" in {
    a1.multipleValues = false
    val output = ListingFieldsRenderer._attributeInputs(Seq(a1), Nil)
    assert(!output.mkString.contains("<input type=\"checkbox\""))
    assert(output.mkString.contains("<select"))
  }
}
