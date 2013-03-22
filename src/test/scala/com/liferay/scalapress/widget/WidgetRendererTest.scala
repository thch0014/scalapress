package com.liferay.scalapress.widget

import org.scalatest.{FunSuite, BeforeAndAfter}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.widgets.{HtmlWidget, Widget, WidgetRenderer}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
class WidgetRendererTest extends FunSuite with MockitoSugar with BeforeAndAfter {

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]

    test("given an object page when restricted and display on all objects then widget is visible") {
        val obj = new Obj
        val sreq = new ScalapressRequest(req, context).withObject(obj)
        val widget = new HtmlWidget
        widget.restricted = true
        widget.displayOnAllObjects = true
        assert(WidgetRenderer.checkWhere(widget, sreq))
    }

    test("given a non root folder page when restricted and display on all folders then widget is visible") {
        val f = new Folder
        f.parent = new Folder
        val sreq = new ScalapressRequest(req, context).withFolder(f)
        val widget = new HtmlWidget
        widget.restricted = true
        widget.displayOnAllFolders = true
        assert(WidgetRenderer.checkWhere(widget, sreq))
    }

    test("given the root page when restricted and display on home then widget is visible") {
        val f = new Folder
        val sreq = new ScalapressRequest(req, context).withFolder(f)
        val widget = new HtmlWidget
        widget.restricted = true
        widget.displayOnHome = true
        assert(WidgetRenderer.checkWhere(widget, sreq))
    }

    test("given the root page when restricted and display on home is false then widget is not visible") {
        val f = new Folder
        val sreq = new ScalapressRequest(req, context).withFolder(f)
        val widget = new HtmlWidget
        widget.restricted = true
        widget.displayOnHome = false
        assert(!WidgetRenderer.checkWhere(widget, sreq))
    }

    test("given a folder page when restricted and the folder is in the excluded list then widget is not visible") {
        val f = new Folder
        f.id = 3
        val sreq = new ScalapressRequest(req, context).withFolder(f)
        val widget = new HtmlWidget
        widget.restricted = true
        widget.excludeFolders = "1,2,3,4"
        assert(!WidgetRenderer.checkWhere(widget, sreq))
    }
}
