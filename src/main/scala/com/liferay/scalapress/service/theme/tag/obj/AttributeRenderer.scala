package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.domain.attr.AttributeValue
import com.liferay.scalapress.enums.AttributeType
import org.joda.time.DateTime

/** @author Stephen Samuel */
object AttributeRenderer {

    def renderValue(av: AttributeValue) = {
        av.attribute.attributeType match {
            case AttributeType.Link => "<a href='" + av.value + "'>" + av.value + "</a>"
            case AttributeType.Email => "<a href='mailto:" + av.value + "'>" + av.value + "</a>"
            case AttributeType.Date => try {
                new DateTime(av.value.toLong).toString("dd/MM/yyyy")
            } catch {
                case e: Exception => av.value
            }
            case AttributeType.DateTime => try {
                new DateTime(av.value.toLong).toString("dd/MM/yyyy HH:mm")
            } catch {
                case e: Exception => av.value
            }
            case _ => av.value.replace("true", "yes").replace("false", "no")
        }
    }
}
