package com.cloudray.scalapress.search.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.search.{FacetTerm, Facet}

/** @author Stephen Samuel */
class FacetRendererTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    import com.github.theon.uri.Uri._

    val uri = parseUri("http://domain.com/search?q=houses")

    val term1 = FacetTerm("Chelsea", 4)

    val facet1 = Facet("area", "15", Seq(term1, FacetTerm("Knightsbridge", 9)))
    val facet2 = Facet("bedrooms", "18", Seq(FacetTerm("1", 4), FacetTerm("2", 8), FacetTerm("3", 2)))

    test("term rendering includes URI") {
        val output = FacetRenderer.renderTerm(facet1, term1, uri)
        assert( """""" === output.toString())
    }

    test("facet rendering sorts by count") {
        val output = FacetRenderer.renderFacet(facet1, uri)
        assert(
            """""" === output
              .toString())
    }

    test("renderer happy path") {
        val output = FacetRenderer.render(Seq(facet1, facet2), uri)
        assert(
            """""" === output
              .toString())
    }
}
