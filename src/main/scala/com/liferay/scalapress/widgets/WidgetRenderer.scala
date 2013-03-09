package com.liferay.scalapress.widgets

import com.liferay.scalapress.{Logging, ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.domain.WidgetContainer

/** @author Stephen Samuel */
object WidgetRenderer extends Logging {

    def render(location: String,
               sep: String,
               request: ScalapressRequest,
               context: ScalapressContext): String = {

        val widgets = context.widgetDao.findAll().filter(location == _.location)
        logger.debug("Loaded {} widgets for {} ...", widgets.size, location)
        val allowed = widgets.filter(_.visible) filter (checkWhere(_, request))
        logger.debug("...and {} are visible on this page", allowed.size)
        val sorted = allowed.sortBy(_.position)
        sorted.flatMap(render(_, request, context)).mkString(sep)
    }

    def checkWhere(widget: Widget, request: ScalapressRequest) = {
        widget.restricted match {
            case false => true
            case true if widget.displayOnAllFolders && request.folder.isDefined
              && request.folder.get.parent != null => true
            case true if widget.displayOnHome && request.folder.isDefined
              && request.folder.get.parent == null => true
            case true if widget.displayOnAllObjects && request.obj.isDefined => true
            case true if widget.displayOnOthers && request.folder.isEmpty && request.obj.isEmpty => true
            case true if request.folder.isDefined && widget
              .whichFolders
              .asScala
              .find(_.id == request.folder.get.id)
              .isDefined => true
            case _ => false
        }
    }

    def normalizedContainerId(widget: Widget) = Option(widget.containerId).getOrElse("widget" + widget.id)
    def widgetContainerClass(widget: Widget) = Option(widget.containerClass).getOrElse("") + " widgetcontainer"

    def render(widget: Widget, req: ScalapressRequest, context: ScalapressContext): Option[String] = {
        widget.render(req, context) match {
            case None => Some("\n<!-- widget " + widget.getClass + " - no content -->\n")
            case Some(body) => {
                val rendered = widget.container match {
                    case WidgetContainer.Table => renderTable(widget, body).toString()
                    case WidgetContainer.Div => renderDiv(widget, body).toString()
                    case _ => body.toString
                }
                Option("\n<!-- widget: " + widget.getClass + "-->\n" + rendered + "\n<!-- end widget -->\n\n")
            }
        }
    }

    def renderDiv(widget: Widget, body: Any) = {
        val capt = Option(widget.caption).map("<h3>" + _ + "</h3>").getOrElse("")
        <div class={widgetContainerClass(widget)} id={normalizedContainerId(widget)}>
            {capt}
            \n
            {body}
        </div>
    }

    def renderTable(widget: Widget, body: Any) = {
        val capt = Option(widget.caption).map("<caption>" + _ + "</caption>").getOrElse("")
        <table cellspacing="0" cellpadding="0" id={normalizedContainerId(widget)} class={widgetContainerClass(widget)}>
            {capt}<tr>
            <td>
                {body}
            </td>
        </tr>
        </table>
    }
}
