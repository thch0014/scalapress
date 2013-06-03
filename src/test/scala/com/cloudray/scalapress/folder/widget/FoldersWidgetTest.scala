package com.cloudray.scalapress.folder.widget

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
class FoldersWidgetTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val widget = new FoldersWidget

    val child1 = new Folder
    child1.name = "captured"
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

}
