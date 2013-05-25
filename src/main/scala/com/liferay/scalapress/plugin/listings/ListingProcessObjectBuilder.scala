package com.liferay.scalapress.plugin.listings

import scala.collection.JavaConverters._
import com.liferay.scalapress.{Logging, ScalapressContext}
import org.joda.time.{DateTimeZone, DateMidnight}
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.obj.attr.AttributeValue
import com.liferay.scalapress.media.Image
import com.liferay.scalapress.plugin.listings.domain.{ListingPackage, ListingProcess}

/** @author Stephen Samuel */
class ListingProcessObjectBuilder(context: ScalapressContext) extends Logging {

    def build(process: ListingProcess): Obj = {
        logger.info("Building listing for process [{}]", process)

        val obj = Obj(process.listingPackage.objectType)
        obj.name = process.title
        logger.debug("Set new listing to use name [{}]", obj.name)
        obj.content = process.content
        obj.status = if (process.listingPackage.autoPublish) Obj.STATUS_LIVE else Obj.STATUS_DISABLED
        obj.expiry = _expiry(process.listingPackage)
        context.objectDao.save(obj)

        process.attributeValues.asScala.foreach(av => {
            val av2 = new AttributeValue
            av2.attribute = av.attribute
            av2.value = av.value
            av2.obj = obj
            obj.attributeValues.add(av2)
        })
        context.objectDao.save(obj)
        logger.debug("... added {} attribute values", obj.attributeValues.size)

        process.folders.foreach(folderId => {
            val folder = context.folderDao.find(folderId)
            folder.objects.add(obj)
            obj.folders.add(folder)
        })
        context.objectDao.save(obj)
        logger.debug("... set inside {} folders", obj.folders.size)

        process.imageKeys.foreach(key => {
            val img = new Image
            img.filename = key
            img.obj = obj
            obj.images.add(img)
        })
        logger.debug("... added {} images", obj.images.size)

        context.objectDao.save(obj)

        obj
    }

    def _expiry(listingPackage: ListingPackage) = new DateMidnight(DateTimeZone.UTC).plusDays(listingPackage.duration).getMillis
}
