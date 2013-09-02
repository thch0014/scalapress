package com.cloudray.scalapress.obj.tag

import com.cloudray.scalapress.{Tag, ScalapressRequest}
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.util.UrlGenerator

/** @author Stephen Samuel */
object ObjectTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {
    request.obj.map(obj => {
      val text = params.get("text").getOrElse(obj.name)
      params.contains("link") match {
        case true => buildLink(UrlGenerator.url(obj), text, params)
        case false => build(text, params)
      }
    })
  }
}

@Tag("summary")
class SummaryTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {

    val max = params.get("max").getOrElse("200").toInt
    val objectContent = request.obj.flatMap(arg => Option(arg.content))
    objectContent.map(_.replaceAll("<.*?>", "")).map(arg => {
      val summary = arg.take(max).reverse.dropWhile(_ != ' ').reverse.trim + "..."
      val tagName = params.get("class").getOrElse("summary")
      build(summary, params + ("class" -> tagName))
    })
  }
}

object LinkTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {

    val folderUrl = request.folder.map(UrlGenerator.url(_))
    val objectUrl = request.obj.map(UrlGenerator.url(_))
    objectUrl.orElse(folderUrl)
  }
}

@Tag("content")
class ContentTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {
    request.obj.map(_.content)
  }
}