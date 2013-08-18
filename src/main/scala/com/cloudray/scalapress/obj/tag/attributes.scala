package com.cloudray.scalapress.obj.tag

import com.cloudray.scalapress.{Tag, Logging, ScalapressRequest}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.obj.attr.{AttributeValue, AttributeFuncs, AttributeValueRenderer, AttributeTableRenderer}

/** @author Stephen Samuel */
@Tag("attribute_value")
class AttributeValueTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val sep = params.get("sep").getOrElse(" ")
    params.get("id") match {
      case None => Some("<!-- no id specified for attribute tag -->")
      case Some(id) => request.obj match {
        case Some(obj) =>

          val values = obj.attributeValues.asScala.toSeq
            .filter(_.attribute.id == id.trim.toLong)
            .sortBy(_.id)
            .map(AttributeValueRenderer.renderValue)

          values.size match {
            case 0 => Option(request.attribute(id.trim.toLong)).flatMap(attr => Option(attr.default))
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

@Tag("attributes_table")
class AttributeTableTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    val excludes = params.get("exclude").map(_.trim.split(",")).getOrElse(Array[String]())
    val includes = params.get("include").map(_.trim.split(",")).getOrElse(Array[String]())

    request.obj match {
      case None => None
      case Some(obj) => {

        // add in default values
        for ( attr <- obj.objectType.attributes.asScala ) {
          Option(attr.default).foreach(default => {
            val qqq = AttributeFuncs.attributeValue(obj, attr)

            if (qqq.isEmpty) {
              val av = new AttributeValue
              av.attribute = attr
              av.obj = obj
              av.value = default
              obj.attributeValues.add(av)
            }
          })
        }

        val avs = obj.sortedAttributeValues
          .filter(_.attribute.public)
          .filterNot(av => excludes.contains(av.attribute.id.toString))
          .filter(av => includes.isEmpty || includes.contains(av.attribute.id.toString))
        avs.size match {
          case 0 => None
          case _ => Some(AttributeTableRenderer.render(avs, params).toString)
        }
      }
    }
  }
}
