package com.liferay.scalapress.service.theme.tag

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
abstract class ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String]
}

trait TagBuilder {

    def buildLink(href: String, label: String, params: Map[String, String]): String = {
        val cssClass = params.get("class").getOrElse("")
        val id = params.get("id").getOrElse("")
        val prefix = params.get("prefix").getOrElse("")
        val suffix = params.get("suffix").getOrElse("")

        val sb = new StringBuilder()
        sb.append(prefix)
        sb.append(label)
        sb.append(suffix)

        <a href={href} class={cssClass} id={id}>
            {sb.toString()}
        </a>.toString()
    }

    def build(body: Any, params: Map[String, String]): String = {

        val tag = params.get("tag")
        val cssClass = params.get("class")
        val prefix = params.get("prefix").getOrElse("")
        val suffix = params.get("suffix").getOrElse("")

        val sb = new StringBuilder()
        if (tag.isDefined) {
            sb.append("<" + tag.get)
            if (cssClass.isDefined) {
                sb.append(" class='" + cssClass.get + "'")
            }
            sb.append(">")
        }


        sb.append(prefix)
        sb.append(body)
        sb.append(suffix)

        if (tag.isDefined)
            sb.append("</" + tag.get + ">")

        sb.mkString
    }
}
