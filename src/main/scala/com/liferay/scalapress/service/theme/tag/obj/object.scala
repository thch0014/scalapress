package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.service.FriendlyUrlGenerator
import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}

/** @author Stephen Samuel */
object ObjectTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        request.obj.map(obj => {

            val text = params.get("label").getOrElse(obj.name)

            params.contains("link") match {
                case true => {

                    val link = FriendlyUrlGenerator.friendlyUrl(obj)
                    <a href={link}>
                        {text}
                    </a>.toString()
                }

                case false => build(obj.name, params)
            }
        })
    }
}

object SummaryTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {

        val max = params.get("max").getOrElse("200").toInt
        val folderContent = request.folder.flatMap(arg => Option(arg.content))
        val objectContent = request.obj.flatMap(arg => Option(arg.content))
        folderContent.orElse(objectContent).map(_.replaceAll("<.*?>", "")).map(arg => {
            val summary = arg.take(max)
            val tagName = params.get("class").getOrElse("summary")
            build(summary, params + ("class" -> tagName))
        })
    }
}

object LinkTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {

        val folderUrl = request.folder.map(FriendlyUrlGenerator.friendlyUrl(_))
        val objectUrl = request.obj.map(FriendlyUrlGenerator.friendlyUrl(_))
        objectUrl.orElse(folderUrl)
    }
}

object ContentTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        request.obj.map(_.content)
    }
}