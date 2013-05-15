package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.listings.controller.process.renderer.ListingsRenderer

/** @author Stephen Samuel */
class ListingsRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val obj1 = new Obj
    obj1.name = "coldplay"
    obj1.status = "live"
    val obj2 = new Obj
    obj2.name = "jethro tull"
    obj2.status = "statto"

    test("mylistings rendering") {
        val actual = ListingsRenderer.myListings(List(obj1, obj2))
        assert(
            "<tableid=\"mylistings\"class=\"tabletable-hovertable-condensed\"><tr><td><ahref=\"/object-0-coldplay\">coldplay</a></td><td>live</td><td>live</td><td><ahref=\"/listing/0\">Edit</a></td></tr><tr><td><ahref=\"/object-0-jethro-tull\">jethrotull</a></td><td>statto</td><td>statto</td><td><ahref=\"/listing/0\">Edit</a></td></tr></table>" === actual
              .toString()
              .replaceAll("\\s", ""))
    }

}
