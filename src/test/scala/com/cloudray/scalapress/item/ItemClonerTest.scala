package com.cloudray.scalapress.item

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.attr.{Attribute, AttributeValue}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class ItemClonerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val itemType = new ItemType
  itemType.id = 15

  val item = new Item
  item.objectType = itemType
  item.name = "battleship"
  item.price = 135
  item.content = "super content"
  item.vatRate = 10.00
  item.exernalReference = "ext-124"
  item.expiry = 554345345
  item.titleTag = "title fight"
  item.keywordsTag = "keyword1 keyword2 spammy"
  item.descriptionTag = "what a lovely desc"
  item.id = 5435

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

  item.attributeValues.add(av1)
  item.attributeValues.add(av2)

  val image1 = "sunderland.jpg"
  val image2 = "sunderland.jpg"

  item.images.add(image1)
  item.images.add(image2)

  val cloner = new ItemCloner()

  test("item clone does not include id") {
    assert(0 === cloner.clone(item).id)
  }

  test("item clone includes name") {
    assert("battleship (Copy)" === cloner.clone(item).name)
  }

  test("item clone includes title tag") {
    assert("title fight" === cloner.clone(item).titleTag)
  }

  test("item clone includes vat rate") {
    assert(10.00 === cloner.clone(item).vatRate)
  }

  test("item clone includes expiry") {
    assert(554345345l === cloner.clone(item).expiry)
  }

  test("item clone includes keywordsTag") {
    assert("keyword1 keyword2 spammy" === cloner.clone(item).keywordsTag)
  }

  test("item clone includes descriptionTag") {
    assert("what a lovely desc" === cloner.clone(item).descriptionTag)
  }

  test("item clone includes exernalReference") {
    assert("ext-124" === cloner.clone(item).exernalReference)
  }
  test("item clone copies object type reference") {
    assert(item.objectType.id > 0)
    assert(item.objectType.id === cloner.clone(item).objectType.id)
  }

  test("item clone includes content") {
    assert("super content" === cloner.clone(item).content)
  }

  test("item clone copies attribute values and updates object reference and resets id") {
    val clone = cloner.clone(item)
    assert(2 === clone.attributeValues.size)
    clone.attributeValues.asScala.foreach(av => {
      assert(clone === av.item)
      assert(0 === av.id)
    })
  }

  test("item clone copies images and updates object reference  and resets id") {
    val clone = cloner.clone(item)
    assert(2 === clone.images.size)
  }
}
