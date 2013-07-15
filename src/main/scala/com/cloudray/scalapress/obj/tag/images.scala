package com.cloudray.scalapress.obj.tag

import com.cloudray.scalapress._
import scala.collection.JavaConverters._
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import scala.Some
import com.cloudray.scalapress.util.UrlGenerator

/** @author Stephen Samuel */

object ImagesTag extends ScalapressTag with TagBuilder with Logging {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    val h = params.get("h").orElse(params.get("height")).getOrElse("0").toInt
    val w = params.get("w").orElse(params.get("width")).getOrElse("0").toInt
    val limit = params.get("limit").getOrElse("100").toInt
    val cssImgClass = params.get("imgclass").getOrElse("")

    val obj = request.obj.orElse(request.line.map(_.obj))
    obj match {
      case None => None
      case Some(o) =>
        val images = o.images.asScala.toSeq
        val sorted = images.sortBy(_.position)
        val rendered = sorted.take(limit).map(i => {
          val src = if (w == 0 || h == 0)
            request.context.assetStore.link(i.filename)
          else
            request.context.imageService.imageLink(i.filename, w, h)

          val html = if (w == 0 || h == 0)
              <img src={src} class={cssImgClass}/>.toString()
          else
              <img src={src} height={h.toString} width={w.toString} class={cssImgClass}/>.toString()

          params.get("link") match {

            case Some(link) if link == "image" =>
              buildLink(request.context.assetStore.link(i.filename), html, params)

            case Some(link) if link == "object" =>
              buildLink(UrlGenerator.url(o), html, params)

            case _ => build(html, params)
          }
        })

        Option(rendered.mkString("\n"))
    }
  }
}

object ImageUrlTag extends ScalapressTag with TagBuilder {

  override def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.obj.map(obj => {
      val limit = params.get("limit").getOrElse("1").toInt
      val rendered = obj.images.asScala.toSeq.sortBy(_.id).take(limit).map(i => "/images/" + i.filename)
      rendered.mkString("\n")
    })
  }
}