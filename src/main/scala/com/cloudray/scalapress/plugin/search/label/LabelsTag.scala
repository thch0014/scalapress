package com.cloudray.scalapress.plugin.search.label

import com.cloudray.scalapress.theme.tag.{TagBuilder, ScalapressTag}
import com.cloudray.scalapress.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("labels")
class LabelsTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.obj.flatMap(obj => Option(obj.labels)) match {
      case Some(labels) if labels.trim.size > 0 =>
        val rendered = labels.split(",").map(label => "<span class='label'>" + label + "</span>")
        Some(rendered.mkString("<br/>"))
      case None => None
    }
  }
}
