package com.cloudray.scalapress.plugin.lightbox.colorbox

import com.cloudray.scalapress.{ScalapressRequest, Tag}
import com.cloudray.scalapress.theme.tag.{TagBuilder, ScalapressTag}
import com.cloudray.scalapress.media.OpType
import com.cloudray.scalapress.util.Scalate

/** @author Stephen Samuel */
@Tag("colorbox")
class ColorboxTag extends ScalapressTag with TagBuilder with OpType {

  override def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    val height = _height(params)
    val width = _width(params)
    val text = params.get("text").getOrElse("")
    request.obj.map(obj => {

      var count = 0
      val links = obj.sortedImages.map(image => {

        val original = request.context.assetStore.link(image)
        val thumb = request.context.thumbnailService.link(image, width, height, _opType(params.get("type")))
        val style = if (count == 0) "" else "display: none"
        count = count + 1

        Scalate.layout("/com/cloudray/scalapress/plugin/lightbox/colorbox/image.ssp",
          Map("original" -> original, "thumb" -> thumb, "style" -> style, "title" -> obj.name,
            "width" -> width, "height" -> height, "text" -> text))
      })

      val script = Scalate.layout("/com/cloudray/scalapress/plugin/lightbox/colorbox/script.ssp")
      links.mkString("\n\n") + script
    })
  }

  def _height(params: Map[String, String]) = params.get("h").orElse(params.get("height")).getOrElse("120").toInt
  def _width(params: Map[String, String]) = params.get("w").orElse(params.get("width")).getOrElse("160").toInt
}