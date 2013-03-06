package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.domain.Obj
import com.liferay.scalapress.domain.attr.AttributeValue
import scala.collection.JavaConverters._
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
class ListingProcess2OrderBuilder(context: ScalapressContext) {

    def build(process: ListingProcess): Obj = {
        val obj = Obj(process.listingPackage.objectType)
        obj.name = process.title

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

        obj
    }
}
