package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.{Logging, ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.service.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel */

object ImagesTag extends ScalapressTag with TagBuilder with Logging {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        val h = params.get("h").orElse(params.get("height")).getOrElse("120").toInt
        val w = params.get("w").orElse(params.get("width")).getOrElse("120").toInt
        val limit = params.get("limit").getOrElse("1").toInt

        val obj = request.obj.orElse(request.line.map(_.obj))
        obj match {

            case None => None
            case Some(o) =>

                val rendered = o.images.asScala.take(limit).map(i => {
                    val tag = "<img src='/images/" +
                      i.filename + "?width=" + w + "&height=" + h + "' height='" + h + "' width='" + w + "'/>"
                    tag
                })

                Option(rendered.mkString("\n"))
        }
    }
}

object ImageUrlTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        request.obj match {

            case None => None
            case Some(obj) =>
                val limit = params.get("limit").getOrElse("1").toInt
                val rendered = obj.images.asScala.take(limit).map(i => "/images/" + i.filename)
                Option(rendered.mkString("\n"))
        }
    }
}

object ColorboxTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        val cssClass = params.get("class").getOrElse("colorboxgroup")
        request.obj.map(obj => {

            var count = 0
            val images = obj.images.asScala.map(image => {

                val link = "/images/" + image.filename
                val text = params.get("text").getOrElse(obj.name)
                val display = if (count == 0) "" else "display: none"
                count = count + 1

                <a class={cssClass} href={link} title={obj.name} display={display}>
                    {text}
                </a>

            }).map(_.toString()).mkString("\n")

            images +
              """<script> $(document).ready(function() {
                    $(".colorboxgroup").colorbox({ rel: '""" + cssClass + """'} );
                </script>"""
        })

    }
}