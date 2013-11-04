package com.cloudray.scalapress.theme.tag

import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("id")
class IdTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val objId = request.item.map(_.id.toString)
    val folderId = request.folder.map(_.id.toString)
    objId.orElse(folderId)
  }
}
