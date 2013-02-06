package com.liferay.scalapress.plugin.search.tag

import com.liferay.scalapress.service.theme.tag.ScalapressTag
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
object TypeaheadTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = None
}
