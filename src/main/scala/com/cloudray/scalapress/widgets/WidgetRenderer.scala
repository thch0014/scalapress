package com.cloudray.scalapress.widgets

import com.cloudray.scalapress.{Logging, ScalapressRequest}

/** @author Stephen Samuel */
object WidgetRenderer extends Logging {

  def render(location: String,
             sep: String,
             request: ScalapressRequest): String = {

    logger.debug("Loading widgets for location={}...", location)
    val widgets = request.widgets.filter(widget => location.equalsIgnoreCase(widget.location))
    logger.debug("... {} widgets loaded", widgets.size)
    val allowed = widgets.filter(_.visible) filter (checkWhere(_, request))
    logger.debug("...and {} are visible on this page", allowed.size)
    val sorted = allowed.sortBy(_.position)
    sorted.flatMap(render(_, request)).mkString(sep)
  }

  def checkWhere(widget: Widget, request: ScalapressRequest) = {
    widget.restricted match {
      case false => true

      case true if request.folder.isDefined &&
        Option(widget.excludeFolders).map(_.split(",").map(_.trim)).getOrElse(Array[String]())
          .contains(request.folder.get.id.toString) => false

      case true if widget.displayOnAllFolders &&
        request.folder.isDefined &&
        request.folder.get.parent != null => true

      case true if request.folder.isDefined &&
        Option(widget.includeFolders).map(_.split(",").map(_.trim)).getOrElse(Array[String]())
          .contains(request.folder.get.id.toString) => true

      case true if widget.displayOnHome &&
        request.folder.isDefined &&
        request.folder.get.parent == null => true

      case true if !widget.displayOnHome &&
        request.folder.isDefined &&
        request.folder.get.parent == null => false

      case true if widget.displayOnAllObjects &&
        request.obj.isDefined => true

      case true if widget.displayOnSearchResults && request.searchResult.isDefined => true

      case true if widget.displayOnOthers &&
        request.folder.isEmpty &&
        request.obj.isEmpty && request.searchResult.isEmpty => true

      case _ => false
    }
  }

  def normalizedContainerId(widget: Widget) = Option(widget.containerId).getOrElse("widget" + widget.id)
  def widgetContainerClass(widget: Widget) = Option(widget.containerClass).getOrElse("") + " widgetcontainer"

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
