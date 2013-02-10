package com.liferay.scalapress.plugin.attachment

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
object AttachmentLinkTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.attachment match {
            case None => None
            case Some(a) => {
                val link = context.assetStore.link(a.filename)
                val text = params.get("text").getOrElse(a.filename)
                val cssClass = params.get("class").getOrElse("attachment_link")

                val xml = <a href={link} class={cssClass}>
                    {text}
                </a>.toString()

                Some(xml.toString)
            }
        }
    }
}

object AttachmentNameTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.attachment match {
            case None => None
            case Some(a) => {
                val name = Option(a.name).getOrElse(a.filename)
                val tag = build(name, params)
                Some(tag)
            }
        }
    }
}