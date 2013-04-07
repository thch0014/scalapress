package com.liferay.scalapress.obj.tag

import com.liferay.scalapress.enums.AttributeType
import org.joda.time.DateTime
import com.liferay.scalapress.obj.attr.AttributeValue

/** @author Stephen Samuel */
object AttributeValueRenderer {

    def renderValue(av: AttributeValue) = {
        av.attribute.attributeType match {
            case AttributeType.Link => <a href={av.value} target="_blank">Please click here</a>.toString()
            case AttributeType.Email => <a href={"mailto:" + av.value}>Email Here</a>.toString()
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
