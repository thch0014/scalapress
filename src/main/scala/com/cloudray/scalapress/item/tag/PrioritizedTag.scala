package com.cloudray.scalapress.item.tag

import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("prioritised")
class PrioritizedTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.item.filter(_.prioritized).map(_ => "prioritized")
  }
}
