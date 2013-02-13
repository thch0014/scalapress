package com.liferay.scalapress

import com.liferay.scalapress.domain.Obj
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object AttributeFuncs {

    def attributeValue(obj: Obj, s: String): Option[String] = {
        obj.attributeValues.asScala.find(_.attribute.name.toLowerCase == s.toLowerCase).map(_.value)
    }

    def attributeValues(obj: Obj, s: String): Seq[String] = {
        obj.attributeValues.asScala.filter(_.attribute.name.toLowerCase == s.toLowerCase).map(_.value)
    }
}
