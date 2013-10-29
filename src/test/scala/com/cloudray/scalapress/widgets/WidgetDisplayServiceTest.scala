package com.cloudray.scalapress.widgets

import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{Cookie, HttpServletRequest}
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.obj.Item
import org.mockito.Mockito

/** @author Stephen Samuel */
class WidgetDisplayServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val context = mock[ScalapressContext]

  val r = SearchResult()
  val sreq = new ScalapressRequest(req, context)

  val widget = new HtmlWidget
  widget.visible = true
  val f = new Folder

  val obj = new Item

  test("given an object page when restricted and display on all objects then widget is visible") {
    val sreq = new ScalapressRequest(req, context)
    widget.restricted = true
    widget.displayOnAllObjects = true
    assert(new WidgetDisplayService().isVisible(widget, sreq.withObject(obj)))
  }

  test("given a non root folder page when restricted and display on all folders then widget is visible") {
    f.parent = new Folder
    widget.restricted = true
    widget.displayOnAllFolders = true
    assert(new WidgetDisplayService().isVisible(widget, sreq.withFolder(f)))
  }

  test("given the root page when restricted and display on home then widget is visible") {
    val sreq = new ScalapressRequest(req, context)
    widget.restricted = true
    widget.displayOnHome = true
    assert(new WidgetDisplayService().isVisible(widget, sreq.withFolder(f)))
  }

  test("given the root page when restricted and display on home is false then widget is not visible") {
    widget.restricted = true
    widget.displayOnHome = false
    assert(!new WidgetDisplayService().isVisible(widget, sreq.withFolder(f)))
  }

  test("given a folder page when restricted and the folder is in the excluded list then widget is not visible") {
    f.id = 3
    widget.restricted = true
    widget.excludeFolders = "1,2,3,4"
    assert(!new WidgetDisplayService().isVisible(widget, sreq.withFolder(f)))
  }

  test("given a non folder/object page and displayOnOthers then the widget is visible") {
    widget.restricted = true
    widget.displayOnOthers = true
    assert(new WidgetDisplayService().isVisible(widget, sreq))
  }

  test("given a non folder/object page and not displayOnOthers then the widget is not visible") {
    val widget = new HtmlWidget
    widget.restricted = true
    widget.displayOnOthers = false
    assert(!new WidgetDisplayService().isVisible(widget, sreq))
  }

  test("given a search results page and show on search results then the widget is visible") {
    widget.restricted = true
    widget.displayOnSearchResults = true
    assert(new WidgetDisplayService().isVisible(widget, sreq.withSearchResult(r)))
  }

  test("given a non search results page and not show on search results then the widget is not visible") {
    widget.restricted = true
    widget.displayOnSearchResults = false
    assert(!new WidgetDisplayService().isVisible(widget, sreq.withSearchResult(r)))
  }

  test("given a one time visible widget and a positive cookie value then the widget is not visible") {
    widget.restricted = false
    widget.oneTimeVisible = true
    widget.id = 13
    Mockito.when(req.getCookies).thenReturn(Array(new Cookie("widgetseen_13", "true")))
    assert(!new WidgetDisplayService().isVisible(widget, sreq.withSearchResult(r)))
  }

  test("given a one time visible widget and a negative cookie value then the widget is visible") {
    widget.restricted = false
    widget.oneTimeVisible = true
    Mockito.when(req.getCookies).thenReturn(Array[Cookie]())
    assert(new WidgetDisplayService().isVisible(widget, sreq.withSearchResult(r)))
  }
}
