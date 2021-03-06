package com.cloudray.scalapress.item.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.attr.{AttributeType, AttributeValueRenderer, Attribute, AttributeValue}

/** @author Stephen Samuel */
class AttributeValueRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val av = new AttributeValue
  av.attribute = new Attribute

  test("text attribute happy path") {
    av.attribute.attributeType = AttributeType.Text
    av.value = "coldplay"
    val rendered = AttributeValueRenderer.renderValue(av)
    assert("coldplay" === rendered)
  }

  test("link attribute happy path") {
    av.attribute.attributeType = AttributeType.Link
    av.value = "www.coldplay.com"
    val rendered = AttributeValueRenderer.renderValue(av)
    assert("<a href=\"www.coldplay.com\" target=\"_blank\">Please click here</a>" === rendered)
  }

  test("email attribute happy path") {
    av.attribute.attributeType = AttributeType.Email
    av.value = "sam@sam.com"
    val rendered = AttributeValueRenderer.renderValue(av)
    assert("<a href=\"mailto:sam@sam.com\">Email Here</a>" === rendered)
  }

  test("email attribute includes bcc when set") {
    av.attribute.attributeType = AttributeType.Email
    av.value = "sam@sam.com"
    av.attribute.bcc = "bcc@blind.com"
    val rendered = AttributeValueRenderer.renderValue(av)
    assert("<a href=\"mailto:sam@sam.com?bcc=bcc@blind.com\">Email Here</a>" === rendered)
  }

  test("email attribute includes cc when set") {
    av.attribute.attributeType = AttributeType.Email
    av.value = "sam@sam.com"
    av.attribute.cc = "copy@cc.com"
    val rendered = AttributeValueRenderer.renderValue(av)
    assert("<a href=\"mailto:sam@sam.com?cc=copy@cc.com\">Email Here</a>" === rendered)
  }

  test("date attribute happy path") {
    av.attribute.attributeType = AttributeType.Date
    av.value = "1000443423423"
    val rendered = AttributeValueRenderer.renderValue(av)
    assert("14/09/2001" === rendered)
  }

  test("datetime attribute happy path") {
    av.attribute.attributeType = AttributeType.DateTime
    av.value = "1000443423423"
    val rendered = AttributeValueRenderer.renderValue(av)
    assert("14/09/2001 04:57" === rendered)
  }
}
