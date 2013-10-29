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
    obj1.objectType = new ItemType
    obj1.objectType.name = "Object"
    obj1.status = Item.STATUS_LIVE

    val obj2 = new Item
    obj2.name = "father"
    obj2.objectType = obj1.objectType
    obj2.status = Item.STATUS_LIVE

    val obj3 = new Item
    obj3.name = "son"
    obj3.objectType = obj1.objectType
    obj3.status = Item.STATUS_LIVE

    TestDatabaseContext.typeDao.save(obj1.objectType)
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
    obj1.objectType = new ItemType
    obj1.objectType.name = "Object"
    obj1.status = Item.STATUS_DELETED

    val obj2 = new Item
    obj2.name = "father"
    obj2.objectType = obj1.objectType
    obj2.status = Item.STATUS_DISABLED

    val obj3 = new Item
    obj3.name = "son"
    obj3.objectType = obj1.objectType
    obj3.status = Item.STATUS_LIVE

    TestDatabaseContext.typeDao.save(obj1.objectType)
    TestDatabaseContext.objectDao.save(obj1)
    TestDatabaseContext.objectDao.save(obj2)
    TestDatabaseContext.objectDao.save(obj3)

    val objs = TestDatabaseContext.objectDao.recent(5)
    assert(!objs.exists(_.status != Item.STATUS_LIVE))
  }
}