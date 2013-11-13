package com.cloudray.scalapress.widgets

import javax.servlet.http.Cookie
import com.cloudray.scalapress.framework.{Logging, ScalapressRequest}
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
class WidgetDisplayService extends Logging {

  def getVisibleWidgets(location: String, sreq: ScalapressRequest): Seq[Widget] = {
    logger.debug("Loading widgets for location={}...", location)
    val widgets = sreq.widgets.filter(widget => location.equalsIgnoreCase(widget.location))
    val allowed = widgets.filter(isVisible(_, sreq))
    logger.debug("...and {} are visible on this page", allowed.size)
    val sorted = allowed.sortBy(_.position)
    sorted
  }

  def getOneTimeCookies(widgets: Iterable[Widget]): Iterable[Cookie] = {
    widgets.filter(_.oneTimeVisible).map(widget => {
      val cookie = new Cookie(cookieName(widget), "true")
      cookie.setPath("/")
      cookie.setMaxAge(60 * 60 * 24 * 365)
      cookie
    })
  }

  def isVisible(widget: Widget, sreq: ScalapressRequest): Boolean =
    widget.visible && checkWhere(widget, sreq) && checkOneTime(widget, sreq) && !isFolderOverride(sreq)

  def isFolderOverride(sreq: ScalapressRequest): Boolean = sreq.folder.exists(isFolderOverride)
  def isFolderOverride(folder: Folder): Boolean = folder.hideWidgets

  private def checkOneTime(widget: Widget, sreq: ScalapressRequest): Boolean = {
    widget.oneTimeVisible match {
      case true if sreq.cookie(cookieName(widget)).isEmpty => true
      case true => false
      case false => true
    }
  }

  private def checkWhere(widget: Widget, sreq: ScalapressRequest): Boolean = {
    widget.restricted match {
      case false => true

      case true if sreq.folder.isDefined &&
        Option(widget.excludeFolders).map(_.split(",").map(_.trim)).getOrElse(Array[String]())
          .contains(sreq.folder.get.id.toString) => false

      case true if widget.displayOnAllFolders &&
        sreq.folder.isDefined &&
        sreq.folder.get.parent != null => true

      case true if sreq.folder.isDefined &&
        Option(widget.includeFolders).map(_.split(",").map(_.trim)).getOrElse(Array[String]())
          .contains(sreq.folder.get.id.toString) => true

      case true if widget.displayOnHome &&
        sreq.folder.isDefined &&
        sreq.folder.get.parent == null => true

      case true if !widget.displayOnHome &&
        sreq.folder.isDefined &&
        sreq.folder.get.parent == null => false

      case true if widget.displayOnAllObjects &&
        sreq.item.isDefined => true

      case true if widget.displayOnSearchResults && sreq.searchResult.isDefined => true

      case true if widget.displayOnOthers &&
        sreq.folder.isEmpty &&
        sreq.item.isEmpty && sreq.searchResult.isEmpty => true

      case _ => false
    }
  }

  private def cookieName(widget: Widget): String = "widgetseen_" + widget.id
}
