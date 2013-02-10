package com.liferay.scalapress

import com.liferay.scalapress.domain.Obj
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object AttributeFuncs {

    def attributeValue(obj: Obj, s: String): Option[String] = {
        obj.attributeValues.asScala.find(_.attribute.name.toLowerCase == s).map(_.value)
    }
}
