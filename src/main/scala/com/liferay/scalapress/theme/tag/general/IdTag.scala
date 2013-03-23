package com.liferay.scalapress.service.theme.tag.general

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
object IdTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val objId = request.obj.map(_.id.toString)
        val folderId = request.folder.map(_.id.toString)
        objId.orElse(folderId)
    }
}