package com.liferay.scalapress.obj.tag

import com.liferay.scalapress.{Tag, Logging, ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel */
@Tag("attribute_value")
class AttributeValueTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val sep = params.get("sep").getOrElse(" ")
        params.get("id") match {
            case None => Some("<!-- no id specified for attribute tag -->")
            case Some(id) => request.obj match {
                case Some(obj) =>

                    val values = obj.attributeValues.asScala.toSeq
                      .filter(_.attribute.id == id.trim.toLong)
                      .sortBy(_.id)
                      .map(AttributeValueRenderer.renderValue(_))

                    values.size match {
                        case 0 => None
                        case _ => Some(build(values.mkString(sep), params))
                    }
                case None => None
            }
        }
    }
}

@Tag("attribute_name")
class AttributeNameTag extends ScalapressTag with TagBuilder with Logging {

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

@Tag("attribute_section")
class AttributeSectionTag extends ScalapressTag with TagBuilder with Logging {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        params.get("id") match {
            case None => None
            case Some(id) => {
                request.obj match {
                    case None => None
                    case Some(obj) =>
                        obj.attributeValues.asScala
                          .find(_.attribute.id == id.trim.toLong)
                          .flatMap(av => Option(av.attribute.section))
                          .map(build(_, params))
                }
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

        request.obj match {
            case None => None
            case Some(obj) => {
                val objects = obj.sortedAttributeValues
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
