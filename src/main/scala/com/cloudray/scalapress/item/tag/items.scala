package com.cloudray.scalapress.item.tag

import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.framework.{UrlGenerator, ScalapressRequest, Tag}

/** @author Stephen Samuel */
object ItemTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {
    val ponly = params.get("prioritizedonly").filter(_ == "1").isDefined
    request.item.filter(_.prioritized || !ponly).map(_render(_, params))
  }

  def _render(item: Item, params: Map[String, String]): String = {
    val text = params.get("text").getOrElse(item.name)
    params.get("link") match {
      case Some(link) if link == "prioritized" && item.prioritized => buildLink(UrlGenerator.url(item), text, params)
      case Some(link) if link != "prioritized" => buildLink(UrlGenerator.url(item), text, params)
      case _ => build(text, params)
    }
  }
}

@Tag("summary")
class SummaryTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {

    val max = params.get("max").getOrElse("200").toInt
    val objectContent = request.item.flatMap(arg => Option(arg.content))
    objectContent.map(_.replaceAll("<.*?>", "")).map(arg => {
      val summary = arg.take(max).reverse.dropWhile(_ != ' ').reverse.trim + "..."
      val tagName = params.get("class").getOrElse("summary")
      build(summary, params + ("class" -> tagName))
    })
  }
}

object LinkTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {

    val folderUrl = request.folder.map(UrlGenerator.url)
    val objectUrl = request.item.map(UrlGenerator.url)
    objectUrl.orElse(folderUrl)
  }
}

@Tag("content")
class ContentTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {
    request.item.map(_.content)
  }
}