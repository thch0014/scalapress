package com.liferay.scalapress.plugin.listings.controller.renderer

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.listings.domain.ListingsPlugin

/** @author Stephen Samuel */
class ListingPackageRendererTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val plugin = new ListingsPlugin

    test("null package text does not break rendering") {
        plugin.packagesPageText = null
        val output = ListingPackageRenderer.render(Nil, plugin).toString()
        assert(output.contains("listing-process-packages"))
    }

    test("package text is included in response") {
        val text = "Please pick a package love"
        plugin.packagesPageText = text
        val output = ListingPackageRenderer.render(Nil, plugin).toString()
        assert(output.contains(text))
    }
}
