package com.cloudray.scalapress.obj.attr

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Item
import scala.util.Random

/** @author Stephen Samuel */
class AttributeTableRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val av1 = new AttributeValue
  av1.attribute = new Attribute
  av1.attribute.id = 123
  av1.attribute.name = "band"
  av1.value = "coldplay"
  av1.attribute.public = true

  val av2 = new AttributeValue
  av2.attribute = new Attribute
  av2.attribute.id = 456
  av2.attribute.name = "singer"
  av2.value = "chris martin"
  av2.attribute.public = true

  val obj = new Item
  obj.attributeValues.add(av1)
  obj.attributeValues.add(av2)

  test("HTML values are rendered without enclosing quotes") {
    av1.value = "<b>coldplay</b>"
    val actual = AttributeTableRenderer._rows(Seq(av1))
    assert( """<tr><td class="attribute-label">band</td><td class="attribute-value"><span><b>coldplay</b></span></td></tr>""" === actual(
      0).toString())
  }

  test("email values are rendered without enclosing quotes") {
    av1.value = "sam@sam.com"
    av1.attribute.attributeType = AttributeType.Email
    av1.attribute.bcc = "bcc@admin.com"
    val actual = AttributeTableRenderer._rows(Seq(av1))
    assert(
      """<tr><td class="attribute-label">band</td><td class="attribute-value"><span><a href="mailto:sam@sam.com?bcc=bcc@admin.com">Email Here</a></span></td></tr>""" === actual(
        0).toString())
  }

  test("link values are rendered without enclosing quotes") {
    av1.value = "http://mysite.com"
    av1.attribute.attributeType = AttributeType.Link
    val actual = AttributeTableRenderer._rows(Seq(av1))
    assert(
      """<tr><td class="attribute-label">band</td><td class="attribute-value"><span><a href="http://mysite.com" target="_blank">Please click here</a></span></td></tr>""" ===
        actual(0).toString())
  }

  test("table includes css from class param") {
    av1.value = "http://mysite.com"
    av1.attribute.attributeType = AttributeType.Link
    val actual = AttributeTableRenderer.render(Seq(av1), Map("class" -> "supertable"))
    assert(actual
      .startsWith( """<table class="attributes attributes-table supertable" cellspacing="0" cellpadding="0">"""))
  }

  test("ordering is stable") {

    val av3 = new AttributeValue
    av3.attribute = new Attribute
    av3.attribute.id = 123
    av3.attribute.name = "guitar"
    av3.value = "johnny"
    av3.attribute.public = true

    val av4 = new AttributeValue
    av4.attribute = new Attribute
    av4.attribute.id = 456
    av4.attribute.name = "bass"
    av4.value = "guy"
    av4.attribute.public = true

    val av5 = new AttributeValue
    av5.attribute = new Attribute
    av5.attribute.id = 123
    av5.attribute.name = "album"
    av5.value = "mylo"
    av5.attribute.public = true

    val av6 = new AttributeValue
    av6.attribute = new Attribute
    av6.attribute.id = 456
    av6.attribute.name = "location"
    av6.value = "uk"
    av6.attribute.public = true

    av1.attribute.position = 15
    av2.attribute.position = 6
    av3.attribute.position = 9
    av4.attribute.position = 2
    av5.attribute.position = 12
    av6.attribute.position = 19

    for ( i <- 1 to 10 ) {
      val seq = Seq(av1, av2, av3, av4, av5, av6)
      val shuffled = Random.shuffle(seq)
      val actual = AttributeTableRenderer._rows(shuffled)
      actual(0).toString().contains("guy")
      actual(1).toString().contains("martin")
      actual(2).toString().contains("johnny")
      actual(3).toString().contains("mylo")
      actual(4).toString().contains("coldplay")
      actual(5).toString().contains("uk")
    }
  }
}
