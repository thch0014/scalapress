package com.cloudray.scalapress.obj.tag

import com.cloudray.scalapress.{ScalapressRequest, Tag}
import com.cloudray.scalapress.theme.tag.ScalapressTag

/** @author Stephen Samuel */
@Tag("prioritised")
class PrioritizedTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.obj.filter(_.prioritized).map(_ => "prioritized")
  }
}
