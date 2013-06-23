package com.cloudray.scalapress.search.controller

import com.cloudray.scalapress.search.{FacetTerm, Facet}
import scala.xml.{Node, Utility}

/** @author Stephen Samuel */
object FacetRenderer {

    def render(facets: Seq[Facet]): Node = {
        val renderedFacets = facets.map(facet => renderFacet(facet))
        val xml = <div id="facets">
            {renderedFacets}
        </div>
        Utility.trim(xml)
    }

    def renderFacet(facet: Facet): Node = {
        val terms = facet.terms.sortBy(_.count).reverse.map(term => renderTerm(facet, term))
        val xml = <div class="search-facet">
            <div class="facet-name">
                {facet.name}
            </div>{terms}
        </div>
        Utility.trim(xml)
    }

    def renderTerm(facet: Facet, term: FacetTerm): Node = {
        val field = facet.field match {
            case id if id.forall(_.isDigit) => "attr_" + id
            case name => name
        }
        val url = s"/search?$field=${term.term}"
        val link = <a href={url}>
            {term.term}
        </a>
        val count = s"(${term.count.toString})"
        val xml = <div class="facet-term">
            {link}&nbsp;{count}
        </div>

        Utility.trim(xml)
    }
}
