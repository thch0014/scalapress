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
@Tag("images")
class ImagesTag extends ScalapressTag with TagBuilder with Logging {

  val DEFAULT_LIMIT = 100

  def _renderImage(i: Image, params: Map[String, String], obj: Obj, context: ScalapressContext) = {

    val t = params.get("type").getOrElse("")
    val cssImgClass = params.get("imgclass").getOrElse("")

    val src = _src(i.filename, _width(params), _height(params), t, context)
    val img = _imageHtml(src, cssImgClass, _width(params), _height(params))

    params.get("link") match {
      case Some(link) if link == "image" => buildLink(context.assetStore.link(i.filename), img, params)
      case Some(link) if link == "object" => buildLink(UrlGenerator.url(obj), img, params)
      case _ => build(img, params)
    }
  }

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.obj.orElse(request.line.map(_.obj)).map(obj => {
      val limit = _limit(params)
      val images = _images(obj, limit)
      val rendered = images.map(i => _renderImage(i, params, obj, request.context))
      rendered.mkString("\n")
    })
  }

  def _height(params: Map[String, String]) = params.get("h").orElse(params.get("height")).getOrElse("0").toInt
  def _width(params: Map[String, String]) = params.get("w").orElse(params.get("width")).getOrElse("0").toInt
  def _limit(params: Map[String, String]) = params.get("limit").map(_.toInt).getOrElse(DEFAULT_LIMIT)
  def _images(obj: Obj, limit: Int): Seq[Image] = obj.images.asScala.toSeq.sortBy(_.position).take(limit)

  def _src(filename: String, w: Int, h: Int, t: String, context: ScalapressContext): String = {
    if (w == 0 || h == 0) context.assetStore.link(filename) // for normal size link directly to CDN
    else context.thumbnailService.link(filename, w, h, t) // thumbnails need conversion work
  }

  def _imageHtml(src: String, klass: String, w: Int, h: Int): Elem = {
    if (w == 0 || h == 0) <img src={Unparsed(src)} class={Unparsed(klass)}/>
    else <img src={Unparsed(src)} height={h.toString} width={w.toString} class={Unparsed(klass)}/>
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