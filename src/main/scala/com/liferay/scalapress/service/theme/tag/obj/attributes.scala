package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{Logging, ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.AttributeType

/** @author Stephen Samuel */
object AttributeValueTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        params.get("id") match {
            case None => Some("<!-- no id specified for attribute tag -->")
            case Some(id) => {
                request.obj.flatMap(obj => {
                    obj.attributeValues.asScala.find(_.id == id.trim.toLong).map(av => {
                        av.attribute.attributeType match {
                            case AttributeType.Link => "<a href='" + av.value + "'>" + av.value + "</a>"
                            case AttributeType.Email => "<a href='mailto:" + av.value + "'>" + av.value + "</a>"
                            case _ => av.value.replace("true", "yes").replace("false", "no")
                        }
                    }).map(build(_, params))
                })
            }
        }
    }
}

object AttributeNameTag extends ScalapressTag with TagBuilder with Logging {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        params.get("id") match {
            case None => None
            case Some(id) => {
                request.obj.map(obj => {
                    val values = obj.attributeValues
                      .asScala
                      .filter(_.id == id.trim.toLong)
                      .map(_.attribute.name)
                      .map(build(_, params))
                    val sep = params.get("sep").getOrElse(", ")
                    build(values.mkString(sep), params)
                })
            }
        }
    }
}

object AttributeTableTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        val excludes = params.get("exclude").map(_.trim.split(",")).getOrElse(Array[String]())
        val includes = params.get("include").map(_.trim.split(",")).getOrElse(Array[String]())

        import scala.collection.JavaConverters._

        request.obj match {
            case None => None
            case Some(obj) => {
                val objects = obj.attributeValues.asScala
                  .filter(_.attribute.public)
                  .filterNot(av => excludes.contains(av.attribute.id.toString))
                  .filter(av => includes.isEmpty || includes.contains(av.attribute.id.toString))
                objects.size match {
                    case 0 => None
                    case _ => Some(AttributeTableRenderer.render(objects).toString)
                }
            }
        }
    }
}
