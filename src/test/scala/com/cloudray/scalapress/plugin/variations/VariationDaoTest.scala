package com.cloudray.scalapress.plugin.variations

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class VariationDaoTest extends FunSuite with MockitoSugar {

  test("persisting a variation sets relationships") {

    val obj = new Obj
    obj.id = 34
    obj.name = "dress"

    val v = new Variation
    v.price = 1234
    v.stock = 5
    v.obj = obj


    TestDatabaseContext.variationDao.save(v)
    assert(v.id > 0)

    val v2 = TestDatabaseContext.variationDao.find(v.id)
    assert(v.stock === v2.stock)
    assert(v.price === v2.price)
    assert(v.obj === v2.obj)
  }
}
