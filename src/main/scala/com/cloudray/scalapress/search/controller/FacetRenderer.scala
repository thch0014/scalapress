package com.cloudray.scalapress.search.controller

import com.cloudray.scalapress.search.{FacetTerm, Facet}
import scala.xml.{Node, Utility}
import com.github.theon.uri.Uri

/** @author Stephen Samuel */
object FacetRenderer {

    def render(facets: Seq[Facet], uri: Uri): Node = {
        val renderedFacets = facets.map(facet => renderFacet(facet, uri))
        val xml = <div id="facets">
            {renderedFacets}
        </div>
        Utility.trim(xml)
    }

    def renderFacet(facet: Facet, uri: Uri): Node = {
        val terms = facet.terms.sortBy(_.count).reverse.map(term => renderTerm(facet, term, uri))
        val xml = <div class="search-facet">
            <div class="facet-name">
                {facet.name}
            </div>{terms}
        </div>
        Utility.trim(xml)
    }

    def renderTerm(facet: Facet, term: FacetTerm, uri: Uri): Node = {
        val field = facet.field match {
            case id if id.forall(_.isDigit) => "attr_" + id
            case name => name
        }
        val url = uri.param(field -> term.term).toString()
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
