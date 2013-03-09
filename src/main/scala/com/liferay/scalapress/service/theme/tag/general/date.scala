package com.liferay.scalapress.service.theme.tag.general

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.joda.time.DateTime
import com.liferay.scalapress.service.theme.tag.ScalapressTag

/** @author Stephen Samuel */
object DateTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        val format = params.get("format").getOrElse("dd/MM/yyyy")
        Some(new DateTime().toString(format))
    }
}