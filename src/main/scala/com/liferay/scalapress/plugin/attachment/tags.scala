package com.liferay.scalapress.plugin.attachment

import com.liferay.scalapress.{Tag, ScalapressRequest}
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel */
@Tag("attachment_link")
class AttachmentLinkTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
        //        request.attachment match {
        //            case None => None
        //            case Some(a) => {
        //                val link = context.assetStore.link(a.filename)
        //                val text = params.get("text").getOrElse(a.filename)
        //                val cssClass = params.get("class").getOrElse("attachment_link")
        //
        //                val xml = <a href={link} class={cssClass}>
        //                    {text}
        //                </a>.toString()
        //
        //                Some(xml.toString)
        //            }
        //        }
        None
    }
}

@Tag("attachment_name")
class AttachmentNameTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
        //        request.attachment match {
        //            case None => None
        //            case Some(a) => {
        //                val name = Option(a.name).getOrElse(a.filename)
        //                val tag = build(name, params)
        //                Some(tag)
        //            }
        //        }
        None
    }
}