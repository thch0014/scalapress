package com.cloudray.scalapress.widgets

import com.cloudray.scalapress.{Tag, Logging, ScalapressRequest}
import com.cloudray.scalapress.theme.tag.ScalapressTag

/** @author Stephen Samuel */
@Tag("widget")
class WidgetsTag extends ScalapressTag with Logging {

  var service = new WidgetDisplayService()
  var renderer = new WidgetRenderer()

  def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = {

    val sep = params.get("sep").getOrElse("")
    params.get("location") match {
      case None =>
        logger.debug("No location set on widgets tag")
        None

      case Some(location) =>

        val widgets = service.getVisibleWidgets(location, sreq)
        val cookies = service.getOneTimeCookies(widgets)
        sreq.cookies.appendAll(cookies)

        val output = renderer.render(widgets, sep, sreq)
        Option(output)
    }
  }
}
