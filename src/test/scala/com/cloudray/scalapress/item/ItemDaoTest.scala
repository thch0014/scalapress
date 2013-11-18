package com.cloudray.scalapress.item

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext
import scala.util.Random
import com.cloudray.scalapress.account.controller.Datum

/** @author Stephen Samuel */
class ItemDaoTest extends FunSuite with MockitoSugar {

  test("saving an object sets updated date") {
    val obj = new Item
    val start = System.currentTimeMillis()
    TestDatabaseContext.itemDao.save(obj)
    val end = System.currentTimeMillis()
    assert(obj.dateUpdated >= start)
    assert(obj.dateUpdated <= end)
  }

  test("persisting an item is assigned id") {

    val obj = new Item
    assert(obj.id == 0)
    TestDatabaseContext.itemDao.save(obj)
    assert(obj.id > 0)
  }

  test("persisting an item can be retrieved by id") {

    val obj = new Item
    obj.name = "super item"
    assert(obj.id == 0)
    TestDatabaseContext.itemDao.save(obj)
    assert(obj.id > 0)
    val obj2 = TestDatabaseContext.itemDao.find(obj.id)
    assert("super item" === obj2.name)
  }

  test("items can be loaded by bulk using ids") {

    val obj1 = new Item
    obj1.name = "coldplay"

    val obj2 = new Item
    obj2.name = "playcold"

    TestDatabaseContext.itemDao.save(obj1)
    TestDatabaseContext.itemDao.save(obj2)

    val objects = TestDatabaseContext.itemDao.findBulk(Seq(obj1.id, obj2.id))
    assert(objects.exists(_.id == obj1.id))
    assert(objects.exists("coldplay" == _.name))
    assert(objects.exists(_.id == obj2.id))
    assert(objects.exists("playcold" == _.name))
  }

  test("recent returns X results in newest id order") {

    val obj1 = new Item
    obj1.name = "grandfather"
    obj1.itemType = new ItemType
    obj1.itemType.name = "Object"
    obj1.status = Item.STATUS_LIVE

    val obj2 = new Item
    obj2.name = "father"
    obj2.itemType = obj1.itemType
    obj2.status = Item.STATUS_LIVE

    val obj3 = new Item
    obj3.name = "son"
    obj3.itemType = obj1.itemType
    obj3.status = Item.STATUS_LIVE

    TestDatabaseContext.typeDao.save(obj1.itemType)
    TestDatabaseContext.itemDao.save(obj1)
    TestDatabaseContext.itemDao.save(obj2)
    TestDatabaseContext.itemDao.save(obj3)

    val objs = TestDatabaseContext.itemDao.recent(2)
    assert(obj3.id === objs(0).id)
    assert(obj2.id === objs(1).id)
  }

  test("recent only includes live objects") {

    val obj1 = new Item
    obj1.name = "grandfather"
    obj1.itemType = new ItemType
    obj1.itemType.name = "Object"
    obj1.status = Item.STATUS_DELETED

    val obj2 = new Item
    obj2.name = "father"
    obj2.itemType = obj1.itemType
    obj2.status = Item.STATUS_DISABLED

    val obj3 = new Item
    obj3.name = "son"
    obj3.itemType = obj1.itemType
    obj3.status = Item.STATUS_LIVE

    TestDatabaseContext.typeDao.save(obj1.itemType)
    TestDatabaseContext.itemDao.save(obj1)
    TestDatabaseContext.itemDao.save(obj2)
    TestDatabaseContext.itemDao.save(obj3)

    val objs = TestDatabaseContext.itemDao.recent(5)
    assert(!objs.exists(_.status != Item.STATUS_LIVE))
  }

  test("search by min price") {

    val obj1 = new Item
    obj1.name = "iphone"
    obj1.price = 50
    obj1.itemType = new ItemType
    obj1.itemType.name = "Object"

    val obj2 = new Item
    obj2.name = "galaxy s3"
    obj2.price = 100
    obj2.itemType = obj1.itemType

    val obj3 = new Item
    obj3.name = "blackberry"
    obj3.price = 0
    obj3.itemType = obj1.itemType

    TestDatabaseContext.typeDao.save(obj1.itemType)
    TestDatabaseContext.itemDao.save(obj1)
    TestDatabaseContext.itemDao.save(obj2)
    TestDatabaseContext.itemDao.save(obj3)

    val q1 = new ItemQuery
    q1.pageSize = 10
    q1.minPrice = Some(49)

    val objs1 = TestDatabaseContext.itemDao.search(q1)
    assert(objs1.results.size === 2)

    val q2 = new ItemQuery
    q2.pageSize = 10
    q2.minPrice = Some(50)

    val objs2 = TestDatabaseContext.itemDao.search(q2)
    assert(objs2.results.size === 2)

    val q3 = new ItemQuery
    q3.pageSize = 10
    q3.minPrice = Some(51)

    val objs3 = TestDatabaseContext.itemDao.search(q3)
    assert(objs3.results.size === 1)
  }

  test("typeahead happy path") {

    val item1 = new Item
    item1.id = Math.abs(Random.nextInt())
    item1.name = "mr merlot"
    item1.status = Item.STATUS_LIVE

    val item2 = new Item
    item2.id = Math.abs(Random.nextInt())
    item2.name = "dr cab"
    item2.status = Item.STATUS_LIVE

    val item3 = new Item
    item3.id = Math.abs(Random.nextInt())
    item3.name = "mr shiraz"
    item3.status = Item.STATUS_LIVE

    val item4 = new Item
    item4.id = Math.abs(Random.nextInt())
    item4.name = "mr malbec"
    item4.status = Item.STATUS_DELETED

    TestDatabaseContext.itemDao.save(item1)
    TestDatabaseContext.itemDao.save(item2)
    TestDatabaseContext.itemDao.save(item3)
    TestDatabaseContext.itemDao.save(item4)

    val datums = TestDatabaseContext.itemDao.typeAhead("mr")
    assert(2 === datums.size)
    assert(datums.contains(Datum(item1.name, item1.id.toString)))
    assert(datums.contains(Datum(item3.name, item3.id.toString)))
  }
}
