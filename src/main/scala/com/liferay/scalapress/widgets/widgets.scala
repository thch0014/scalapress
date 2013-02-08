package com.liferay.scalapress.widgets

import com.liferay.scalapress.service.theme.tag.ScalapressTag
import com.liferay.scalapress.{Logging, ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.widgets.WidgetRenderer

/** @author Stephen Samuel */
object WidgetsTag extends ScalapressTag with Logging {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        val sep = params.get("sep").getOrElse("")
        params.get("location") match {
            case None =>
                logger.debug("No location set on widgets tag")
                None
            case Some(location) =>
                val output = WidgetRenderer.render(location, sep, request, context)
                Option(output)
        }
    }
}
