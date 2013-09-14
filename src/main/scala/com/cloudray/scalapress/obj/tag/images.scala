package com.cloudray.scalapress.obj.tag

import com.cloudray.scalapress._
import scala.collection.JavaConverters._
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import scala.Some
import com.cloudray.scalapress.util.UrlGenerator
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.media.Image
import scala.xml.{Unparsed, Elem}

/** @author Stephen Samuel */

object ImagesTag extends ScalapressTag with TagBuilder with Logging {

  def _images(obj: Obj, limit: Int): Seq[Image] = obj.images.asScala.toSeq.sortBy(_.position).take(limit)
  def _src(filename: String, w: Int, h: Int, t: String, context: ScalapressContext): String = {
    if (w == 0 || h == 0) context.assetStore.link(filename)
    else context.thumbnailService.link(filename, w, h, t)
  }
  def _img(src: String, klass: String, w: Int, h: Int): Elem = {
    if (w == 0 || h == 0)
        <img src={Unparsed(src)} class={klass}/>
    else
        <img src={Unparsed(src)} height={h.toString} width={w.toString} class={klass}/>
  }

  def _renderImage(i: Image, params: Map[String, String], obj: Obj, context: ScalapressContext) = {

    val h = params.get("h").orElse(params.get("height")).getOrElse("0").toInt
    val w = params.get("w").orElse(params.get("width")).getOrElse("0").toInt
    val t = params.get("type").getOrElse("")
    val cssImgClass = params.get("imgclass").getOrElse("")

    val src = _src(i.filename, w, h, t, context)
    val img = _img(src, cssImgClass, w, h)

    params.get("link") match {
      case Some(link) if link == "image" => buildLink(context.assetStore.link(i.filename), img, params)
      case Some(link) if link == "object" => buildLink(UrlGenerator.url(obj), img, params)
      case _ => build(img, params)
    }
  }

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.obj.orElse(request.line.map(_.obj)).map(obj => {

      val limit = params.get("limit").getOrElse("100").toInt
      val images = Nil//_images(obj, limit)
      val rendered = images.map(i => _renderImage(i, params, obj, request.context))
      rendered.mkString("\n")

    })
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