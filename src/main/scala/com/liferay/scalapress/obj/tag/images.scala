package com.liferay.scalapress.obj.tag

import com.liferay.scalapress._
import scala.collection.JavaConverters._
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import scala.Some

/** @author Stephen Samuel */

object ImagesTag extends ScalapressTag with TagBuilder with Logging {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        val h = params.get("h").orElse(params.get("height")).getOrElse("0").toInt
        val w = params.get("w").orElse(params.get("width")).getOrElse("0").toInt
        val limit = params.get("limit").getOrElse("100").toInt

        val obj = request.obj.orElse(request.line.map(_.obj))
        obj match {
            case None => None
            case Some(o) =>
                val images = o.images.asScala.toSeq
                val sorted = images.sortBy(_.position)
                val rendered = sorted.take(limit).map(i => {
                    val src = if (w == 0 || h == 0)
                        context.assetStore.link(i.filename)
                    else
                        context.imageService.imageLink(i.filename, w, h)

                    val html = if (w == 0 || h == 0)
                            <img src={src}/>.toString()
                    else
                            <img src={src} height={h.toString} width={w.toString}/>.toString()

                    params.get("link") match {

                        case Some(link) if link == "image" =>
                            buildLink(context.assetStore.link(i.filename), html, params)

                        case Some(link) if link == "object" =>
                            buildLink(FriendlyUrlGenerator.friendlyUrl(o), html, params)

                        case _ => build(html, params)
                    }
                })

                Option(rendered.mkString("\n"))
        }
    }
}

object ImageUrlTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        request.obj match {

            case None => None
            case Some(obj) =>
                val limit = params.get("limit").getOrElse("1").toInt
                val rendered = obj.images.asScala.take(limit).map(i => "/images/" + i.filename)
                Option(rendered.mkString("\n"))
        }
    }
}

@Tag("colorbox")
class ColorboxTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        val height = params.get("height").getOrElse("120").toInt
        val width = params.get("width").getOrElse("160").toInt
        val text = params.get("text").getOrElse("")
        request.obj.map(obj => {

            var count = 0
            val images = obj.images.asScala.toSeq
            val sorted = images.sortBy(_.position)
            val rendered = obj.images.asScala.map(image => {

                val original = context.assetStore.link(image.filename)
                val thumb = context.imageService.imageLink(image.filename, width, height)
                val display = if (count == 0) "" else "display: none"
                count = count + 1

                "<a class='colorboxgroup' href='" + original + "' title='" + obj
                  .name + "' style='" + display + "'><img src='" + thumb + "' width='" + width
                  .toString + "' height='" + height + "'/><span>" + text + "</span></a> "

            }).mkString("\n")

            rendered +
              """<script>
                 $(document).ready(function() {
                    $(".colorboxgroup").colorbox({ rel: 'colorboxgroup', top: '150px' });
                 });
                </script>"""
        })

    }
}