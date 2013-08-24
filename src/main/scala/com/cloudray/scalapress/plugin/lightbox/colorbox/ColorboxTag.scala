package com.cloudray.scalapress.plugin.lightbox.colorbox

import com.cloudray.scalapress.{ScalapressRequest, Tag}
import com.cloudray.scalapress.theme.tag.{TagBuilder, ScalapressTag}
import org.fusesource.scalate.TemplateEngine

/** @author Stephen Samuel */
@Tag("colorbox")
class ColorboxTag extends ScalapressTag with TagBuilder {

  val engine = new TemplateEngine

  override def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    val t = params.get("type").getOrElse("")
    val height = params.get("height").getOrElse("120").toInt
    val width = params.get("width").getOrElse("160").toInt
    val text = params.get("text").getOrElse("")
    request.obj.map(obj => {

      var count = 0
      val links = obj.sortedImages.map(image => {

        val original = request.context.assetStore.link(image.filename)
        val thumb = request.context.thumbnailService.link(image.filename, width, height, t)
        val style = if (count == 0) "" else "display: none"
        count = count + 1

        engine.layout("/com/cloudray/scalapress/plugin/lightbox/colorbox/image.ssp",
          Map("original" -> original, "thumb" -> thumb, "style" -> style, "title" -> obj.name,
            "width" -> width, "height" -> height, "text" -> text))
      })

      val script = engine.layout("/com/cloudray/scalapress/plugin/lightbox/colorbox/script.ssp")
      links.mkString("\n\n") + script
    })
  }
}