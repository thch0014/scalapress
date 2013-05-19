package com.liferay.scalapress.obj.tag

import com.liferay.scalapress.{Tag, ScalapressRequest}
import com.liferay.scalapress.theme.tag.{ScalapressTag2, TagBuilder}
import com.liferay.scalapress.plugin.friendlyurl.FriendlyUrlGenerator

/** @author Stephen Samuel */
object ObjectTag extends ScalapressTag2 with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {
        request.obj.map(obj => {
            val text = params.get("text").getOrElse(obj.name)
            params.contains("link") match {
                case true => buildLink(FriendlyUrlGenerator.friendlyUrl(obj), text, params)
                case false => build(text, params)
            }
        })
    }
}

@Tag("summary")
class SummaryTag extends ScalapressTag2 with TagBuilder {
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

object LinkTag extends ScalapressTag2 with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {

        val folderUrl = request.folder.map(FriendlyUrlGenerator.friendlyUrl(_))
        val objectUrl = request.obj.map(FriendlyUrlGenerator.friendlyUrl(_))
        objectUrl.orElse(folderUrl)
    }
}

@Tag("content")
class ContentTag extends ScalapressTag2 with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]) = {
        request.obj.map(_.content)
    }
}