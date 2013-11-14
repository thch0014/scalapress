package com.cloudray.scalapress.item

import com.cloudray.scalapress.search.ItemRef

/** @author Stephen Samuel */
class ItemBulkLoader(itemDao: ItemDao) {

  def loadFromRefs(refs: Seq[ItemRef]): Seq[Item] = loadFromIds(refs.map(_.id))
  def loadFromIds(ids: Seq[Long]): Seq[Item] = {
    val items = itemDao.findBulk(ids).groupBy(_.id)
    ids.map(items(_).head)
  }
}
