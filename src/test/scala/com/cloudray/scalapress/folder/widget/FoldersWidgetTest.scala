package com.cloudray.scalapress.folder.widget

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.Folder
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class FoldersWidgetTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val widget = new FoldersWidget
    widget.id = 15

    val child1 = new Folder
    child1.name = "captured"
    child1.id = 1
    val child2 = new Folder
    child2.name = "plans"
    val child3 = new Folder
    child3.name = "escaped"
    val child4 = new Folder
    child4.name = "z love           spaces me"

    val parent = new Folder
    parent.subfolders.add(child1)
    parent.subfolders.add(child2)
    parent.subfolders.add(child3)
    parent.subfolders.add(child4)

    widget.start = parent

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext

    test("backoffice url is absolute") {
        assert(widget.backoffice.startsWith("/backoffice"))
    }

    test("exclusions splits up on comma and new line") {
        widget.exclusions = "hannibal\nface man\nba,murdock"
        val actual = widget._exclusions
        assert(Seq("hannibal", "face man", "ba", "murdock") === actual)
    }

    test("children excludes exclusions") {
        widget.exclusions = "escaped"
        val actual = widget._children(parent)
        assert(3 === actual.size)
        assert(child1 === actual(0))
        assert(child2 === actual(1))
        assert(child4 === actual(2))
    }

    test("children excludes hidden") {
        child2.hidden = true
        val actual = widget._children(parent)
        assert(3 === actual.size)
        assert(child1 === actual(0))
        assert(child3 === actual(1))
        assert(child4 === actual(2))
    }

    test("folder exclusions ignores repeated whitespace") {
        widget.exclusions = "escaped,   z  love spaces     me  "
        println(widget._exclusions)
        val actual = widget._children(parent)
        assert(2 === actual.size)
        assert(child1 === actual(0))
        assert(child2 === actual(1))
    }

    test("folder exclusions ignores case") {
        widget.exclusions = "EScAPED"
        println(widget._exclusions)
        val actual = widget._children(parent)
        assert(3 === actual.size)
        assert(child1 === actual(0))
        assert(child2 === actual(1))
        assert(child4 === actual(2))
    }

    test("folder widget excludes null names") {

        assert(4 === widget._children(parent).size)

        val child5 = new Folder
        child5.name = null
        parent.subfolders.add(child5)

        assert(4 === widget._children(parent).size)
    }

    test("folder rendering of li uses correct class and id") {
        val actual = widget._renderFolder(child1, 4)
        assert("<li class=\"l4\" id=\"w15_f1\"><a href=\"/folder-1-captured\">captured</a></li>" === actual.toString())
    }

    test("folder rendering happy path") {
        val actual = widget.render(ScalapressRequest(req, context))
        assert(
            "<ul class=\"widget-folder-plugin\"><li class=\"l1\" id=\"w15_f1\"><a href=\"/folder-1-captured\">captured</a></li><li class=\"l1\" id=\"w15_f0\"><a href=\"/folder-0-escaped\">escaped</a></li><li class=\"l1\" id=\"w15_f0\"><a href=\"/folder-0-plans\">plans</a></li><li class=\"l1\" id=\"w15_f0\"><a href=\"/folder-0-z-love-spaces-me\">z love           spaces me</a></li></ul>" === actual
              .get)
    }
}
