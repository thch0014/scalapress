package com.cloudray.scalapress.plugin.variations

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class DimensionValueDaoTest extends FunSuite with MockitoSugar {

  test("persisting a dimension value sets value") {

    val dv = new DimensionValue
    dv.value = "red"

    TestDatabaseContext.dimensionValueDao.save(dv)
    assert(dv.id > 0)

    val dv2 = TestDatabaseContext.dimensionValueDao.find(dv.id)
    assert(dv.value === dv2.value)
  }
}
