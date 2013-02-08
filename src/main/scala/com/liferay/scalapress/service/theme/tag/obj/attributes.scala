package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

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

object AttributeNameTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        params.get("id") match {
            case None => None
            case Some(id) => {
                import scala.collection.JavaConverters._
                request.obj.flatMap(obj => {
                    obj.attributeValues.asScala.find(_.id == id.toLong).map(_.attribute.name).map(build(_, params))
                })
            }
        }
    }
}

object AttributeTableTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        val excludes = params.get("exclude").getOrElse("").split(",")
        val includes = params.get("include").getOrElse("").split(",")

        import scala.collection.JavaConverters._

        request.obj match {
            case None => None
            case Some(obj) =>
                val objects = obj.attributeValues.asScala
                  .filter(av => excludes.isEmpty || !excludes.contains(av.id.toString))
                  .filter(av => includes.isEmpty || includes.contains(av.id.toString))
                AttributeTableRenderer.render(objects)
        }
    }
}
