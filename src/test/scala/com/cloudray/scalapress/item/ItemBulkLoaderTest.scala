package com.cloudray.scalapress.item

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito

/** @author Stephen Samuel */
class ItemBulkLoaderTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val itemDao = mock[ItemDao]
  val loader = new ItemBulkLoader(itemDao)

  "an item bulk loader" should "retain original order" in {

    val i1 = new Item
    i1.id = 1
    val i2 = new Item
    i2.id = 2
    val i3 = new Item
    i3.id = 3
    val i4 = new Item
    i4.id = 4
    val i5 = new Item
    i5.id = 5

    Mockito.when(itemDao.findBulk(Seq(1, 2, 3, 4, 5))).thenReturn(Seq(i5, i4, i3, i2, i1))
    val items = loader.loadFromIds(Seq(1, 2, 3, 4, 5))
    assert(Seq(i1, i2, i3, i4, i5) === items)
  }
}
