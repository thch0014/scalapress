package com.cloudray.scalapress.obj.tag

import com.cloudray.scalapress._
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.util.UrlGenerator
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.media._
import scala.xml.{Unparsed, Elem}
import scala.Some

/** @author Stephen Samuel */
@Tag("images")
class ImagesTag extends ScalapressTag with TagBuilder with Logging {

  val DEFAULT_LIMIT = 100

  def _opType(option: Option[String]) = option match {
    case Some("bound") => Bound
    case Some("cover") => Cover
    case _ => Fit
  }

  def _renderImage(i: String, params: Map[String, String], obj: Obj, context: ScalapressContext) = {

    val opType = _opType(params.get("type"))
    val cssImgClass = params.get("imgclass").getOrElse("")

    val src = _src(i, _width(params), _height(params), opType, context)
    val img = _imageHtml(src, cssImgClass, _width(params), _height(params))

    params.get("link") match {
      case Some(link) if link == "image" => buildLink(context.assetStore.link(i), img, params)
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
  def _images(obj: Obj, limit: Int): Seq[String] = obj.sortedImages.take(limit)

  def _src(filename: String, w: Int, h: Int, opType: OpType, context: ScalapressContext): String = {
    if (w == 0 || h == 0) context.assetStore.link(filename) // for normal size link directly to CDN
    else context.thumbnailService.link(filename, w, h, opType) // thumbnails need conversion work
  }

  def _imageHtml(src: String, klass: String, w: Int, h: Int): Elem = {
    if (w == 0 || h == 0) <img src={Unparsed(src)} class={klass}/>
    else <img src={Unparsed(src)} height={h.toString} width={w.toString} class={klass}/>
  }
}

object ImageUrlTag extends ScalapressTag with TagBuilder {

  override def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.obj.map(obj => {
      val limit = params.get("limit").getOrElse("1").toInt
      val rendered = obj.sortedImages.take(limit).map(i => "/images/" + i)
      rendered.mkString("\n")
    })
  }
}