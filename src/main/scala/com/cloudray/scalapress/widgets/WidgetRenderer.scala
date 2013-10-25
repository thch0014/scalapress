package com.cloudray.scalapress.widgets

import com.cloudray.scalapress.{Logging, ScalapressRequest}

/** @author Stephen Samuel */
class WidgetRenderer extends Logging {

  def render(widgets: Seq[Widget], sep: String, sreq: ScalapressRequest): String = {
    widgets.flatMap(render(_, sreq)).mkString(sep)
  }

  def render(widget: Widget, req: ScalapressRequest): Option[String] = {
    logger.debug("Rendering widget {}...", widget)
    val start = System.currentTimeMillis()
    val result = widget.render(req) match {
      case None => Some("\n<!-- widget " + widget.getClass + " - no content -->\n")
      case Some(body) => {
        val rendered = widget.container match {
          case WidgetContainer.Table => renderTable(widget, body)
          case WidgetContainer.Div => renderDiv(widget, body)
          case _ => body.toString
        }
        Option("\n<!-- widget: " + widget.getClass + "-->\n" + rendered + "\n<!-- end widget -->\n\n")
      }
    }
    logger.debug("...completed in {} ms", System.currentTimeMillis() - start)
    result
  }

  def normalizedContainerId(widget: Widget) = Option(widget.containerId).getOrElse("widget" + widget.id)
  def widgetContainerClass(widget: Widget) = Option(widget.containerClass).getOrElse("") + " widgetcontainer"

  def renderDiv(widget: Widget, body: Any): String = {
    val capt = Option(widget.caption).map("<h3>" + _ + "</h3>").getOrElse("")
    "<div class='" + widgetContainerClass(widget) + "' id='" + normalizedContainerId(widget) + "'>" + capt + body + "</div>"
  }

  def renderTable(widget: Widget, body: Any): String = {

    val cssClass = widgetContainerClass(widget)
    val id = normalizedContainerId(widget)

    "<table cellspacing='0' cellpadding='0' id='" + id + "' class='" + cssClass + "'><caption>" + Option(widget
      .caption)
      .getOrElse("") + "</caption><tr><td>" + body + "</td></tr></table>"
  }
}
