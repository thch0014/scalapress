package com.cloudray.scalapress.item

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.TestDatabaseContext

/** @author Stephen Samuel */
class ItemTypeDaoTest extends FunSuite with MockitoSugar {

  test("persisting an item can be retrieved by id") {

    val t = new ItemType
    t.name = "my type"

    assert(t.id == null)
    TestDatabaseContext.typeDao.save(t)
    assert(t.id > 0)

    val t2 = TestDatabaseContext.typeDao.find(t.id)
    assert("my type" === t2.name)
  }
}
