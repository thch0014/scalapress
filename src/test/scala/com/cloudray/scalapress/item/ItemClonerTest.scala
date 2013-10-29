package com.cloudray.scalapress.item

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.attr.{Attribute, AttributeValue}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class ItemClonerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val objectType = new ItemType
  objectType.id = 15

  val obj = new Item
  obj.objectType = objectType
  obj.name = "battleship"
  obj.price = 135
  obj.content = "super content"
  obj.vatRate = 10.00
  obj.exernalReference = "ext-124"
  obj.expiry = 554345345
  obj.titleTag = "title fight"
  obj.keywordsTag = "keyword1 keyword2 spammy"
  obj.descriptionTag = "what a lovely desc"
  obj.id = 5435

  val av1 = new AttributeValue
  av1.attribute = new Attribute
  av1.attribute.id = 1
  av1.value = "mackams"
  av1.id = 345

  val av2 = new AttributeValue
  av2.attribute = new Attribute
  av2.attribute.id = 2
  av2.value = "middlesbrough"
  av2.id = 555

  obj.attributeValues.add(av1)
  obj.attributeValues.add(av2)

  val image1 = "sunderland.jpg"
  val image2 = "sunderland.jpg"

  obj.images.add(image1)
  obj.images.add(image2)

  val cloner = new ItemCloner()

  test("object clone does not include id") {
    assert(0 === cloner.clone(obj).id)
  }

  test("object clone includes name") {
    assert("battleship (Copy)" === cloner.clone(obj).name)
  }

  test("object clone includes title tag") {
    assert("title fight" === cloner.clone(obj).titleTag)
  }

  test("object clone includes vat rate") {
    assert(10.00 === cloner.clone(obj).vatRate)
  }

  test("object clone includes expiry") {
    assert(554345345l === cloner.clone(obj).expiry)
  }

  test("object clone includes keywordsTag") {
    assert("keyword1 keyword2 spammy" === cloner.clone(obj).keywordsTag)
  }

  test("object clone includes descriptionTag") {
    assert("what a lovely desc" === cloner.clone(obj).descriptionTag)
  }

  test("object clone includes exernalReference") {
    assert("ext-124" === cloner.clone(obj).exernalReference)
  }
  test("object clone copies object type reference") {
    assert(obj.objectType.id > 0)
    assert(obj.objectType.id === cloner.clone(obj).objectType.id)
  }



  test("object clone includes content") {
    assert("super content" === cloner.clone(obj).content)
  }

  test("object clone copies attribute values and updates object reference and resets id") {
    val clone = cloner.clone(obj)
    assert(2 === clone.attributeValues.size)
    clone.attributeValues.asScala.foreach(av => {
      assert(clone === av.obj)
      assert(0 === av.id)
    })
  }

  test("object clone copies images and updates object reference  and resets id") {
    val clone = cloner.clone(obj)
    assert(2 === clone.images.size)
  }
}
