package com.cloudray.scalapress.obj

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class ObjDaoTest extends FunSuite with MockitoSugar {

  test("saving an object sets updated date") {
    val obj = new Obj
    val start = System.currentTimeMillis()
    TestDatabaseContext.objectDao.save(obj)
    val end = System.currentTimeMillis()
    assert(obj.dateUpdated >= start)
    assert(obj.dateUpdated <= end)
  }

  test("persisting an obj is assigned id") {

    val obj = new Obj
    assert(obj.id == 0)
    TestDatabaseContext.objectDao.save(obj)
    assert(obj.id > 0)
  }

  test("persisting an obj can be retrieved by id") {

    val obj = new Obj
    obj.name = "super obj"
    assert(obj.id == 0)
    TestDatabaseContext.objectDao.save(obj)
    assert(obj.id > 0)
    val obj2 = TestDatabaseContext.objectDao.find(obj.id)
    assert("super obj" === obj2.name)
  }

  test("objects can be loaded by bulk using ids") {

    val obj1 = new Obj
    obj1.name = "coldplay"

    val obj2 = new Obj
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

    val obj1 = new Obj
    obj1.name = "grandfather"
    obj1.objectType = new ObjectType
    obj1.objectType.name = "Object"
    obj1.status = Obj.STATUS_LIVE

    val obj2 = new Obj
    obj2.name = "father"
    obj2.objectType = obj1.objectType
    obj2.status = Obj.STATUS_LIVE

    val obj3 = new Obj
    obj3.name = "son"
    obj3.objectType = obj1.objectType
    obj3.status = Obj.STATUS_LIVE

    TestDatabaseContext.typeDao.save(obj1.objectType)
    TestDatabaseContext.objectDao.save(obj1)
    TestDatabaseContext.objectDao.save(obj2)
    TestDatabaseContext.objectDao.save(obj3)

    val objs = TestDatabaseContext.objectDao.recent(2)
    assert(obj3.id === objs(0).id)
    assert(obj2.id === objs(1).id)
  }

  test("recent only includes live objects") {

    val obj1 = new Obj
    obj1.name = "grandfather"
    obj1.objectType = new ObjectType
    obj1.objectType.name = "Object"
    obj1.status = Obj.STATUS_DELETED

    val obj2 = new Obj
    obj2.name = "father"
    obj2.objectType = obj1.objectType
    obj2.status = Obj.STATUS_DISABLED

    val obj3 = new Obj
    obj3.name = "son"
    obj3.objectType = obj1.objectType
    obj3.status = Obj.STATUS_LIVE

    TestDatabaseContext.typeDao.save(obj1.objectType)
    TestDatabaseContext.objectDao.save(obj1)
    TestDatabaseContext.objectDao.save(obj2)
    TestDatabaseContext.objectDao.save(obj3)

    val objs = TestDatabaseContext.objectDao.recent(5)
    assert(!objs.exists(_.status != Obj.STATUS_LIVE))
  }
}
