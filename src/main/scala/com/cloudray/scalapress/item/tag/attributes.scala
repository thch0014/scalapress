package com.cloudray.scalapress.item.tag

import scala.collection.JavaConverters._
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.item.attr.{AttributeValue, AttributeFuncs, AttributeValueRenderer, AttributeTableRenderer}
import com.cloudray.scalapress.framework.{Logging, ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("attribute_value")
class AttributeValueTag extends ScalapressTag with TagBuilder {

  def isAllDigits(x: String) = x forall Character.isDigit

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val ponly = params.get("prioritizedonly").filter(_ == "1").isDefined
    val sep = params.get("sep").getOrElse(" ")
    params.get("id") match {
      case Some(id) if isAllDigits(id.trim) => request.item.filter(_.prioritized || !ponly) match {
        case Some(obj) =>

          val values = obj.attributeValues.asScala.toSeq
            .filter(_.attribute.id == id.trim.toLong)
            .sortBy(_.id)
            .map(AttributeValueRenderer.renderValue)

          values.size match {
            case 0 => Option(request.attribute(id.trim.toLong)).flatMap(attr => Option(attr.default))
            case _ => Some(build(values.mkString(sep), params))
          }

        case _ => None
      }
      case _ => None
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
        request.item.flatMap(obj => {
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

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    params.get("id") match {
      case None => None
      case Some(id) => {
        request.item match {
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

    request.item match {
      case None => None
      case Some(item) => {

        // add in default values
        for ( attr <- item.itemType.attributes.asScala ) {
          Option(attr.default).foreach(default => {
            val qqq = AttributeFuncs.attributeValue(item, attr)

            if (qqq.isEmpty) {
              val av = new AttributeValue
              av.attribute = attr
              av.item = item
              av.value = default
              item.attributeValues.add(av)
            }
          })
        }

        val avs = item.sortedAttributeValues
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
