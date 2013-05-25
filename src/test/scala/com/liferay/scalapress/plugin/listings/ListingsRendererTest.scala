package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.listings.controller.renderer.ListingsRenderer

/** @author Stephen Samuel */
class ListingsRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val obj1 = new Obj
    obj1.id = 63
    obj1.name = "coldplay"
    obj1.status = "live"
    val obj2 = new Obj
    obj2.id = 15
    obj2.name = "jethro tull"
    obj2.status = "statto"

    test("rendering contains listing names") {
        val actual = ListingsRenderer.myListings(List(obj1, obj2)).toString()

        assert(actual.contains("coldplay"))
        assert(actual.contains("jethro tull"))
    }

    test("rendering contains listing statuses") {
        val actual = ListingsRenderer.myListings(List(obj1, obj2)).toString()

        assert(actual.contains("live"))
        assert(actual.contains("statto"))
    }

    test("rendering contains headings") {
        val actual = ListingsRenderer.myListings(List(obj1, obj2)).toString()

        assert(actual.contains("Listing"))
        assert(actual.contains("Expiry Date"))
    }

    test("rendering contains edit link") {
        val actual = ListingsRenderer.myListings(List(obj1, obj2)).toString()

        assert(actual.contains("/listing/" + obj1.id))
        assert(actual.contains("/listing/" + obj2.id))
    }
}
