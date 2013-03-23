package com.liferay.scalapress.search.tag

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.theme.tag.ScalapressTag

/** @author Stephen Samuel */
object TypeaheadTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = None
}
