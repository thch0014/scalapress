package com.cloudray.scalapress.obj.attr

import AttributeType
import org.joda.time.{DateTimeZone, DateTime}
import scala.collection.mutable.ArrayBuffer

/** @author Stephen Samuel */
object AttributeValueRenderer {

    def _renderemail(av: AttributeValue) = {
        val params = new ArrayBuffer[String]()
        Option(av.attribute.cc).foreach(arg => params.append("cc=" + arg))
        Option(av.attribute.bcc).foreach(arg => params.append("bcc=" + arg))

        val sb = new StringBuffer()
        sb.append("mailto:" + av.value)
        if (params.size > 0) {
            sb.append("?")
            sb.append(params.mkString("&"))
        }

        <a href={sb.toString}>Email Here</a>.toString()
    }

    def renderValue(av: AttributeValue) = {
        av.attribute.attributeType match {
            case AttributeType.Link => <a href={av.value} target="_blank">Please click here</a>.toString()
            case AttributeType.Email => _renderemail(av)
            case AttributeType.Date => try {
                new DateTime(av.value.toLong, DateTimeZone.UTC).toString("dd/MM/yyyy")
            } catch {
                case e: Exception => av.value
            }
            case AttributeType.DateTime => try {
                new DateTime(av.value.toLong, DateTimeZone.UTC).toString("dd/MM/yyyy HH:mm")
            } catch {
                case e: Exception => av.value
            }
            case _ => av.value.replace("true", "yes").replace("false", "no")
        }
    }
}
