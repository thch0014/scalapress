package com.liferay.scalapress.plugin.listings

import scala.collection.JavaConverters._
import com.liferay.scalapress.{Logging, ScalapressContext}
import org.joda.time.DateMidnight
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.obj.attr.AttributeValue
import com.liferay.scalapress.media.Image

/** @author Stephen Samuel */
class ListingProcess2ObjectBuilder(context: ScalapressContext) extends Logging {

    def build(process: ListingProcess): Obj = {
        logger.info("Building listing for process [{}]", process)

        val obj = Obj(process.listingPackage.objectType)
        obj.name = process.title
        obj.content = process.content
        obj.status = if (process.listingPackage.autoPublish) "Live" else "Disabled"
        obj.account = context.objectDao.find(process.accountId.toLong)
        obj.expiry = new DateMidnight().plusDays(process.listingPackage.duration).getMillis
        context.objectDao.save(obj)

        process.attributeValues.asScala.foreach(av => {
            val av2 = new AttributeValue
            av2.attribute = av.attribute
            av2.value = av.value
            av2.obj = obj
            obj.attributeValues.add(av2)
        })
        context.objectDao.save(obj)

        process.folders.foreach(folderId => {
            val folder = context.folderDao.find(folderId)
            folder.objects.add(obj)
            obj.folders.add(folder)
        })
        context.objectDao.save(obj)

        process.imageKeys.foreach(key => {
            val img = new Image
            img.filename = key
            img.obj = obj
            obj.images.add(img)
        })
        context.objectDao.save(obj)

        obj
    }
}
