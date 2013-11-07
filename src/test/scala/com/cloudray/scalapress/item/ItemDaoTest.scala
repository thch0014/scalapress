package com.cloudray.scalapress.item

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class ItemDaoTest extends FunSuite with MockitoSugar {

  test("saving an object sets updated date") {
    val obj = new Item
    val start = System.currentTimeMillis()
    TestDatabaseContext.objectDao.save(obj)
    val end = System.currentTimeMillis()
    assert(obj.dateUpdated >= start)
    assert(obj.dateUpdated <= end)
  }

  test("persisting an item is assigned id") {

    val obj = new Item
    assert(obj.id == 0)
    TestDatabaseContext.objectDao.save(obj)
    assert(obj.id > 0)
  }

  test("persisting an item can be retrieved by id") {

    val obj = new Item
    obj.name = "super item"
    assert(obj.id == 0)
    TestDatabaseContext.objectDao.save(obj)
    assert(obj.id > 0)
    val obj2 = TestDatabaseContext.objectDao.find(obj.id)
    assert("super item" === obj2.name)
  }

  test("items can be loaded by bulk using ids") {

    val obj1 = new Item
    obj1.name = "coldplay"

    val obj2 = new Item
    obj2.name = "playcold"

    TestDatabaseContext.objectDao.save(obj1)
    TestDatabaseContext.objectDao.save(obj2)

    val objects = TestDatabaseContext.objectDao.findBulk(Seq(obj1.id, obj2.id))
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
    TestDatabaseContext.objectDao.save(obj1)
    TestDatabaseContext.objectDao.save(obj2)
    TestDatabaseContext.objectDao.save(obj3)

    val objs = TestDatabaseContext.objectDao.recent(2)
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

    TestDatabaseContext.typeDao.save(obj1.objectType)
    TestDatabaseContext.objectDao.save(obj1)
    TestDatabaseContext.objectDao.save(obj2)
    TestDatabaseContext.objectDao.save(obj3)

    val objs = TestDatabaseContext.objectDao.recent(5)
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
    TestDatabaseContext.objectDao.save(obj1)
    TestDatabaseContext.objectDao.save(obj2)
    TestDatabaseContext.objectDao.save(obj3)

    val q1 = new ItemQuery
    q1.pageSize = 10
    q1.minPrice = Some(1)

    val objs1 = TestDatabaseContext.objectDao.search(q1)
    assert(objs1.results.size === 2)

    val q2 = new ItemQuery
    q2.pageSize = 10
    q2.minPrice = Some(50)

    val objs2 = TestDatabaseContext.objectDao.search(q2)
    assert(objs2.results.size === 2)

    val q3 = new ItemQuery
    q3.pageSize = 10
    q3.minPrice = Some(51)

    val objs3 = TestDatabaseContext.objectDao.search(q3)
    assert(objs3.results.size === 1)
  }
}
