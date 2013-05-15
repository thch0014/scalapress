package com.liferay.scalapress.plugin.search.label

import com.liferay.scalapress.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("labels")
class LabelsTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.obj.flatMap(obj => Option(obj.labels)) match {
            case Some(labels) if labels.trim.size > 0 =>
                val rendered = labels.split(",").map(label => "<span class='label'>" + label + "</span>")
                Some(rendered.mkString("<br/>"))
            case None => None
        }
    }
}
