package com.liferay.scalapress.obj.tag

import com.liferay.scalapress.{Logging, ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel */
object AttributeValueTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        params.get("id") match {
            case None => Some("<!-- no id specified for attribute tag -->")
            case Some(id) => {
                request.obj.flatMap(obj => {
                    obj.attributeValues.asScala.find(_.attribute.id == id.trim.toLong) match {
                        case None => None
                        case Some(av) => Some(build(AttributeValueRenderer.renderValue(av), params))
                    }
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
                request.obj.flatMap(obj => {
                    obj.attributeValues.asScala
                      .find(_.attribute.id == id.trim.toLong)
                      .map(av => build(av.attribute.name, params))
                })
            }
        }
    }
}

object AttributeSectionTag extends ScalapressTag with TagBuilder with Logging {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        params.get("id") match {
            case None => None
            case Some(id) => {
                request.obj.flatMap(obj => {
                    obj.attributeValues.asScala
                      .find(_.attribute.id == id.trim.toLong)
                      .map(av => build(av.attribute.section, params))
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
                    case _ => Some(AttributeTableRenderer.render(objects.toSeq).toString)
                }
            }
        }
    }
}
