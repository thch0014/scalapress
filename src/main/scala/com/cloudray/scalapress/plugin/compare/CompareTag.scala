package com.cloudray.scalapress.plugin.compare

import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.framework.{Tag, ScalapressRequest}

/** @author Stephen Samuel */
@Tag("compare")
class CompareTag extends ScalapressTag {

  def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = {
    sreq.item.map(arg => <button id={"compare-" + arg.id} class="btn btn-default">Compare</button>).map(_.toString())
  }
}
