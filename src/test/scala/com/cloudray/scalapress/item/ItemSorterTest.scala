package com.cloudray.scalapress.item

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.attr.{Attribute, AttributeValue}
import com.cloudray.scalapress.search.Sort

/** @author Stephen Samuel */
class ItemSorterTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val obj1 = new Item
  obj1.id = 8
  obj1.name = "coldplay"

  val av1 = new AttributeValue
  av1.attribute = new Attribute
  av1.value = "chris"
  obj1.attributeValues.add(av1)

  val obj2 = new Item
  obj2.id = 5
  obj2.name = "keane"

  val av2 = new AttributeValue
  av2.attribute = new Attribute
  av2.value = "tom"
  obj2.attributeValues.add(av2)

  val obj3 = new Item
  obj3.id = 9
  obj3.name = "cream"

  val av3 = new AttributeValue
  av3.attribute = new Attribute
  av3.value = "jack"
  obj3.attributeValues.add(av3)

  val obj4 = new Item
  obj4.id = 1
  obj4.name = "jethro tull"

  val av4 = new AttributeValue
  av4.attribute = new Attribute
  av4.value = "ian"
  obj4.attributeValues.add(av4)

  val obj5 = new Item
  obj5.id = 7
  obj5.name = "elton john"

  val av5 = new AttributeValue
  av5.attribute = new Attribute
  av5.value = "elton"
  obj5.attributeValues.add(av5)

  val objs = Seq(obj1, obj2, obj3, obj4, obj5)

  test("Newest sort sorts by id asc") {
    val sorted = ItemSorter.sort(objs, Sort.Newest, None)
    assert(Seq(obj3, obj1, obj5, obj2, obj4) === sorted)
  }

  test("Oldest sort sorts by id asc") {
    val sorted = ItemSorter.sort(objs, Sort.Oldest, None)
    assert(Seq(obj4, obj2, obj5, obj1, obj3) === sorted)
  }

  test("Attribute sort uses values from attributes") {
    val sorted = ItemSorter.sort(objs, Sort.Attribute, Some(av1.attribute))
    assert(Seq(obj1, obj5, obj4, obj3, obj2) === sorted)
  }

  test("Attribute sort without specifying attribute defaults to name sort") {
    val sorted = ItemSorter.sort(objs, Sort.Attribute, None)
    assert(Seq(obj1, obj3, obj5, obj4, obj2) === sorted)
  }

  test("random maintains order for the same hash code") {
    val initial = ItemSorter.sort(objs, Sort.Random, None, "someseed".hashCode)
    for ( i <- 1 to 5 )
      assert(ItemSorter.sort(objs, Sort.Random, None, "someseed".hashCode) === initial)
    for ( i <- 1 to 5 )
      assert(ItemSorter.sort(objs, Sort.Random, None, "otherseed".hashCode) != initial)
  }
}
