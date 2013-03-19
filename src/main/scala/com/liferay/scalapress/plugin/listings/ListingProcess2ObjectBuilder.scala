package com.liferay.scalapress.plugin.listings

import scala.collection.JavaConverters._
import com.liferay.scalapress.ScalapressContext
import org.joda.time.DateMidnight
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.obj.attr.AttributeValue
import com.liferay.scalapress.media.Image

/** @author Stephen Samuel */
class ListingProcess2ObjectBuilder(context: ScalapressContext) {

    def build(process: ListingProcess, account: Obj): Obj = {

        val obj = Obj(process.listingPackage.objectType)
        obj.name = process.title
        obj.content = process.content
        obj.status = "Disabled"
        obj.account = account
        obj.expiry = new DateMidnight().plusDays(process.listingPackage.duration).getMillis
        context.objectDao.save(obj)

        process.attributeValues.asScala.foreach(av => {
            val av2 = new AttributeValue
            av2.attribute = av.attribute
            av2.value = av.value
            av2.obj = obj
            obj.attributeValues.add(av2)
        })

        process.folders.foreach(folderId => {
            val folder = context.folderDao.find(folderId)
            folder.objects.add(obj)
            obj.folders.add(folder)
        })

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
