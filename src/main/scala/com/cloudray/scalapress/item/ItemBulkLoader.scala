package com.cloudray.scalapress.item

/** @author Stephen Samuel */
class ItemBulkLoader(itemDao: ItemDao) {

  def load(ids: Seq[Long]) = {
    val items = itemDao.findBulk(ids).groupBy(_.id)
    ids.map(items(_).head)
  }
}
