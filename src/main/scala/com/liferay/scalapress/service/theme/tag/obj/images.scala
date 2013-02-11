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
                    val src = context.imageService.imageLink(i.filename, w, h)
                        <img src={src} height={h.toString} width={w.toString}/>.toString()
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

        val height = params.get("height").getOrElse("120").toInt
        val width = params.get("width").getOrElse("160").toInt
        val text = params.get("text").getOrElse("")
        request.obj.map(obj => {

            var count = 0
            val images = obj.images.asScala.map(image => {

                val original = context.assetStore.link(image.filename)
                val thumb = context.imageService.imageLink(image.filename, width, height)
                val display = if (count == 0) "" else "display: none"
                count = count + 1

                <a class="colorboxgroup" href={original} title={obj.name} style={display}>
                    <img src={thumb} width={width.toString} height={height.toString}/>
                    <span>
                        {text}
                    </span>
                </a>

            }).map(_.toString()).mkString("\n")

            images +
              """<script>
                 $(document).ready(function() {
                    $(".colorboxgroup").colorbox({ rel: 'colorboxgroup' });
                 });
                </script>"""
        })

    }
}