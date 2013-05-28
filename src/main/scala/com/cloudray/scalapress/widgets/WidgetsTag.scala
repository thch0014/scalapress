package com.cloudray.scalapress.widgets

import com.cloudray.scalapress.{Tag, Logging, ScalapressRequest}
import com.cloudray.scalapress.theme.tag.ScalapressTag

/** @author Stephen Samuel */
@Tag("widgets")
class WidgetsTag extends ScalapressTag with Logging {

    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

        val sep = params.get("sep").getOrElse("")
        params.get("location") match {
            case None =>
                logger.debug("No location set on widgets tag")
                None
            case Some(location) =>
                val output = WidgetRenderer.render(location, sep, request, request.context)
                Option(output)
        }
    }
}