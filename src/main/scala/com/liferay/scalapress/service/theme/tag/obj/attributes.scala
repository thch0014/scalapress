package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{Logging, ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object AttributeValueTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        params.get("id") match {
            case None => None
            case Some(id) => {
                import scala.collection.JavaConverters._
                request.obj.flatMap(obj => {
                    obj.attributeValues.asScala.find(_.id == id.toLong).map(_.value).map(build(_, params))
                })
            }
        }
    }
}

object AttributeNameTag extends ScalapressTag with TagBuilder with Logging {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        params.get("id") match {
            case None => None
            case Some(id) => {
                try {
                    request.obj.flatMap(obj => {
                        obj.attributeValues
                          .asScala
                          .find(_.id == id.toLong)
                          .map(_.attribute.name)
                          .map(build(_, params))
                    })
                } catch {
                    case e: Exception =>
                        logger.warn("{}", e)
                        None
                }
            }
        }
    }
}

object AttributeTableTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        val excludes = params.get("exclude").map(_.split(",")).getOrElse(Array[String]())
        val includes = params.get("include").map(_.split(",")).getOrElse(Array[String]())

        import scala.collection.JavaConverters._

        request.obj match {
            case None => None
            case Some(obj) => {
                val objects = obj.attributeValues.asScala
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
