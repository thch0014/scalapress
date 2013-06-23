package com.cloudray.scalapress.search.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.search.{FacetTerm, Facet}

/** @author Stephen Samuel */
class FacetRendererTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val facet1 = Facet("area", "15", Seq(FacetTerm("Chelsea", 4), FacetTerm("Knightsbridge", 9)))
    val facet2 = Facet("bedrooms", "18", Seq(FacetTerm("1", 4), FacetTerm("2", 8), FacetTerm("3", 2)))

    test("facet rendering sorts by count") {
        val output = FacetRenderer.renderFacet(facet1)
        assert(
            """<div class="search-facet"><div class="facet-name">area</div><div class="facet-term">Knightsbridge&nbsp;(9)</div><div class="facet-term">Chelsea&nbsp;(4)</div></div>""" === output
              .toString())
    }

    test("renderer happy path") {
        val output = FacetRenderer.render(Seq(facet1, facet2))
        assert(
            """<div id="facets"><div class="search-facet"><div class="facet-name">area</div><div class="facet-term">Knightsbridge&nbsp;(9)</div><div class="facet-term">Chelsea&nbsp;(4)</div></div><div class="search-facet"><div class="facet-name">bedrooms</div><div class="facet-term">2&nbsp;(8)</div><div class="facet-term">1&nbsp;(4)</div><div class="facet-term">3&nbsp;(2)</div></div></div>""" === output
              .toString())
    }
}
