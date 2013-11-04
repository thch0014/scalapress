package com.cloudray.scalapress.plugin.listings

import scala.collection.JavaConverters._
import org.joda.time.{DateTimeZone, DateMidnight}
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.item.attr.AttributeValue
import com.cloudray.scalapress.plugin.listings.domain.{ListingPackage, ListingProcess}
import com.cloudray.scalapress.framework.{Logging, ScalapressContext}

/** @author Stephen Samuel */
class ListingProcessItemBuilder(context: ScalapressContext) extends Logging {

  def build(process: ListingProcess): Item = {
    logger.debug("Building listing for process [{}]", process)

    val item = _item(process)

    process.attributeValues.asScala.foreach(av => item.attributeValues.add(_av(av, item)))
    context.itemDao.save(item)
    logger.debug("... added {} attribute values", item.attributeValues.size)

    process.folders.foreach(folderId => {
      val folder = context.folderDao.find(folderId)
      folder.objects.add(item)
      item.folders.add(folder)
    })
    context.itemDao.save(item)
    logger.debug("... set inside {} folders", item.folders.size)

    process.imageKeys.foreach(key => item.images add key)
    logger.debug("... added {} images", item.images.size)

    context.itemDao.save(item)

    item
  }

  def _item(process: ListingProcess): Item = {
    val item = Item(process.listingPackage.objectType)
    item.name = process.title
    logger.debug("Set new listing to use name [{}]", item.name)

    item.listingPackage = process.listingPackage
    item.content = process.content
    item.status = Item.STATUS_DISABLED
    item.expiry = _expiry(process.listingPackage)
    item.labels = process.listingPackage.labels
    context.itemDao.save(item)
    item
  }

  def _av(av: AttributeValue, item: Item): AttributeValue = {
    val av2 = new AttributeValue
    av2.attribute = av.attribute
    av2.value = av.value
    av2.item = item
    av2
  }

  def _expiry(listingPackage: ListingPackage) =
    if (listingPackage.duration == 0) 0l
    else new DateMidnight(DateTimeZone.UTC).plusDays(listingPackage.duration).getMillis
}
