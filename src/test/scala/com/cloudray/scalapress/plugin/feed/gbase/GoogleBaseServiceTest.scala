package com.cloudray.scalapress.plugin.feed.gbase

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class GoogleBaseServiceTest extends FunSuite with MockitoSugar {

  test("that objects can be retrieved") {

    val obj1 = new Item
    obj1.name = "grandfather"
    obj1.status = Item.STATUS_LIVE
    obj1.price = 10

    val obj2 = new Item
    obj2.name = "father"
    obj2.status = Item.STATUS_DELETED
    obj2.price = 12

    val obj3 = new Item
    obj3.name = "son"
    obj3.status = Item.STATUS_LIVE
    obj3.price = 0

    TestDatabaseContext.objectDao.save(obj1)
    TestDatabaseContext.objectDao.save(obj2)
    TestDatabaseContext.objectDao.save(obj3)

    val objs = GoogleBaseService._objects(TestDatabaseContext.objectDao)
    assert(objs.exists(_.id == obj1.id))
    assert(!objs.exists(_.id == obj2.id))
    assert(!objs.exists(_.id == obj3.id))
  }
}
