package com.cloudray.scalapress.plugin.listings

import scala.collection.JavaConverters._
import com.cloudray.scalapress.{Logging, ScalapressContext}
import org.joda.time.{DateTimeZone, DateMidnight}
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.item.attr.AttributeValue
import com.cloudray.scalapress.plugin.listings.domain.{ListingPackage, ListingProcess}

/** @author Stephen Samuel */
class ListingProcessObjectBuilder(context: ScalapressContext) extends Logging {

  def build(process: ListingProcess): Item = {
    logger.debug("Building listing for process [{}]", process)

    val obj = _obj(process)

    process.attributeValues.asScala.foreach(av => obj.attributeValues.add(_av(av, obj)))
    context.objectDao.save(obj)
    logger.debug("... added {} attribute values", obj.attributeValues.size)

    process.folders.foreach(folderId => {
      val folder = context.folderDao.find(folderId)
      folder.objects.add(obj)
      obj.folders.add(folder)
    })
    context.objectDao.save(obj)
    logger.debug("... set inside {} folders", obj.folders.size)

    process.imageKeys.foreach(key => obj.images add key)
    logger.debug("... added {} images", obj.images.size)

    context.objectDao.save(obj)

    obj
  }

  def _obj(process: ListingProcess): Item = {
    val obj = Item(process.listingPackage.objectType)
    obj.name = process.title
    logger.debug("Set new listing to use name [{}]", obj.name)

    obj.listingPackage = process.listingPackage
    obj.content = process.content
    obj.status = Item.STATUS_DISABLED
    obj.expiry = _expiry(process.listingPackage)
    obj.labels = process.listingPackage.labels
    context.objectDao.save(obj)
    obj
  }

  def _av(av: AttributeValue, obj: Item): AttributeValue = {
    val av2 = new AttributeValue
    av2.attribute = av.attribute
    av2.value = av.value
    av2.obj = obj
    av2
  }

  def _expiry(listingPackage: ListingPackage) =
    if (listingPackage.duration == 0)
      0l
    else
      new DateMidnight(DateTimeZone.UTC)
        .plusDays(listingPackage.duration)
        .getMillis
}
