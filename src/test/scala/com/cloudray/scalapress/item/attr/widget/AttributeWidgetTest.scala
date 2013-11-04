package com.cloudray.scalapress.item.attr.widget

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.attr.{AttributeOption, Attribute}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.ItemType
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class AttributeWidgetTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val attribute = new Attribute
  attribute.id = 3
  attribute.name = "bands"
  attribute.objectType = new ItemType
  attribute.objectType.id = 9

  val option1 = new AttributeOption
  option1.value = "coldplay"
  option1.attribute = attribute
  attribute.options.add(option1)

  val option2 = new AttributeOption
  option2.value = "keane"
  option2.attribute = attribute
  attribute.options.add(option2)

  val widget = new AttributeWidget
  widget.attribute = attribute

  val context = new ScalapressContext
  val req = ScalapressRequest(mock[HttpServletRequest], context)

  "an attribute widget" should "render values with links" in {
    val rendered = widget.render(req).get
    assert(rendered.contains("/search?type=9&attr_3=coldplay"))
    assert(rendered.contains("/search?type=9&attr_3=keane"))
  }

  it should "render with a classname of attribute-widget" in {
    val rendered = widget.render(req).get
    assert(rendered.startsWith("<ul class='attribute-widget'>"))
  }
}
