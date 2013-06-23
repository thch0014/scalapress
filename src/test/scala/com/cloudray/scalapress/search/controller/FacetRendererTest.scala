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
        assert( """<div class="facet-term"><a href="http://domain.com/search?q=houses&amp;attr_15=Chelsea">Chelsea</a>&nbsp;(4)</div>""" === output
          .toString())
    }

    test("facet rendering sorts by count") {
        val output = FacetRenderer.renderFacet(facet1, uri)
        assert(
            """<div class="search-facet"><div class="facet-name">area</div><div class="facet-term"><a href="http://domain.com/search?q=houses&amp;attr_15=Knightsbridge">Knightsbridge</a>&nbsp;(9)</div><div class="facet-term"><a href="http://domain.com/search?q=houses&amp;attr_15=Chelsea">Chelsea</a>&nbsp;(4)</div></div>""" === output
              .toString())
    }

    test("renderer happy path") {
        val output = FacetRenderer.render(Seq(facet1, facet2), uri)
        assert(
            """<div id="facets"><div class="search-facet"><div class="facet-name">area</div><div class="facet-term"><a href="http://domain.com/search?q=houses&amp;attr_15=Knightsbridge">Knightsbridge</a>&nbsp;(9)</div><div class="facet-term"><a href="http://domain.com/search?q=houses&amp;attr_15=Chelsea">Chelsea</a>&nbsp;(4)</div></div><div class="search-facet"><div class="facet-name">bedrooms</div><div class="facet-term"><a href="http://domain.com/search?q=houses&amp;attr_18=2">2</a>&nbsp;(8)</div><div class="facet-term"><a href="http://domain.com/search?q=houses&amp;attr_18=1">1</a>&nbsp;(4)</div><div class="facet-term"><a href="http://domain.com/search?q=houses&amp;attr_18=3">3</a>&nbsp;(2)</div></div></div>""" === output
              .toString())
    }
}
