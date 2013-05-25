package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.listings.domain.ListingsPlugin
import com.liferay.scalapress.plugin.listings.controller.renderer.ListingPackageRenderer

/** @author Stephen Samuel */
class ListingPackageRendererTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val plugin = new ListingsPlugin
    plugin.packagesPageText = "I love <b>html</b>"

    test("that HTML is rendered inside the package header") {
        val actual = ListingPackageRenderer.render(Nil, plugin)
        assert("<div id=\"listing-process-packages\">I love <b>html</b></div>" === actual
          .toString()
          .replaceAll("\\s{2,}", "").trim)
    }
}
