package com.cloudray.scalapress.item.controller.admin

import org.scalatest.mock.MockitoSugar
import org.scalatest.{OneInstancePerTest, FunSuite}
import com.cloudray.scalapress.item.{ItemType, TypeDao}
import org.mockito.Mockito

/** @author Stephen Samuel */
class TypeListControllerTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val dao = mock[TypeDao]
  val controller = new TypeListController(dao)

  test("types do not include deleted") {
    val type1 = new ItemType
    type1.deleted = false
    val type2 = new ItemType
    type2.deleted = true
    Mockito.when(dao.findAll).thenReturn(List(type1, type2))
    val types = controller.types
    assert(1 === types.size)
    assert(type1 === types.get(0))
  }

  test("deleting an object type sets deleted flag to true and persists") {
    val type1 = new ItemType
    type1.deleted = false
    Mockito.when(dao.find(14)).thenReturn(type1)
    controller.delete(14)
    Mockito.verify(dao).save(type1)
    assert(type1.deleted)
  }
}
