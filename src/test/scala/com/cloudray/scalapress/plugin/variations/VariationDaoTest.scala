package com.cloudray.scalapress.plugin.variations

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext
import com.cloudray.scalapress.item.Item

/** @author Stephen Samuel */
class VariationDaoTest extends FunSuite with MockitoSugar {

  test("persisting a variation sets relationships") {

    val obj = new Item
    obj.name = "dress"

    TestDatabaseContext.objectDao.save(obj)
    assert(obj.id > 0)

    val v = new Variation
    v.price = 1234
    v.stock = 5
    v.obj = obj

    TestDatabaseContext.variationDao.save(v)
    assert(v.id > 0)

    val v2 = TestDatabaseContext.variationDao.find(v.id)
    assert(v.stock === v2.stock)
    assert(v.price === v2.price)
    assert(v.obj.id === v2.obj.id)
  }

  test("find by object") {

    val obj = new Item
    obj.name = "dress"

    TestDatabaseContext.objectDao.save(obj)
    assert(obj.id > 0)

    val v = new Variation
    v.price = 1234
    v.stock = 5
    v.obj = obj

    TestDatabaseContext.variationDao.save(v)
    assert(v.id > 0)

    val v2 = TestDatabaseContext.variationDao.findByObjectId(obj.id)
    assert(1 === v2.size)
    assert(v.id === v2(0).id)
  }
}
