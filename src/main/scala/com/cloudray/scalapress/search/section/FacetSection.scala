package com.cloudray.scalapress.search.section

import com.cloudray.scalapress.framework.ScalapressRequest
import javax.persistence._
import scala.beans.BeanProperty
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.theme.MarkupRenderer
import com.cloudray.scalapress.item.{Item, ItemDao}
import scala.xml.Node
import com.github.theon.uri.Uri
import com.cloudray.scalapress.search.AttributeFacetField
import com.cloudray.scalapress.search.Facet
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.util.{Scalate, UrlParser}

/** @author Stephen Samuel */
@Entity
@Table(name = "search_section_facet")
class FacetSection extends SearchResultsSection {

  override def desc: String = "Facet browser for a folders items"
  override def backoffice: String = "/backoffice/search/section/facet/" + id

  @Column(name = "attributes")
  @BeanProperty
  var attributes: String = _

  override def render(sreq: ScalapressRequest): Option[String] = sreq.folder.map(folder => render(folder, sreq))

  private def render(folder: Folder, sreq: ScalapressRequest): String = {

    val uri = UrlParser.parse(sreq)
    val selections = FacetSelectionParser.parse(sreq)
    val search = createSearch(folder, selections, facetFields)

    val searchService = sreq.context.bean[SearchService]
    val result = searchService.search(search)

    renderSelectedFacets(selections, uri) + renderFacets(result, uri) + renderItems(result, sreq)
  }

  private def renderSelectedFacets(selected: Seq[FacetSelection], uri: Uri): String = {
    "<div class='search-selected-facets'>" +
      selected.map(renderSelectedFacet(_, uri)).mkString("\n") +
      "</div>"
  }

  private def field2name(field: FacetField): String = field match {
    case AttributeFacetField(_) => "Attribute Name Here"
    case TagsFacetField => "Tags"
    case _ => ""
  }

  private def renderSelectedFacet(selected: FacetSelection, uri: Uri): String = {
    val urlWithFacetRemoved = uri.removeParams(selected.field.key)
    "<div class='search-selected-facet'>" + selected.value + " <a href=" + urlWithFacetRemoved + ">x</a>" + "</div>"
  }

  private def renderFacets(result: SearchResult, uri: Uri): String = {
    val facetsWithMultipleTerms = result.facets.filter(_.terms.size > 1)
    "<div class='search-facets'>" + facetsWithMultipleTerms.map(renderFacet(_, uri)).mkString("\n") + "</div>"
  }

  private def renderFacet(facet: Facet, uri: Uri): Node = {
    val terms = facet.terms.map(term =>
      Scalate.layout(
        "/com/cloudray/scalapress/search/section/facetterm.ssp",
        Map("count" -> term.count,
          "value" -> term.value,
          "uri" -> uri.replaceParams(facet.field.key, term.value)
        )
      )
    )
    <div class="search-facet">
      <h6>
        {facet.name}
      </h6>{terms}
    </div>
  }

  private def renderItems(result: SearchResult, sreq: ScalapressRequest): String = {
    val items = sreq.context.bean[ItemDao].findBulk(result.refs.map(_.id))
    "<div class='search-items'>" + items.map(renderItem(_, sreq)).mkString("\n") + "</div>"
  }

  private def renderItem(item: Item, sreq: ScalapressRequest) = {
    val m = Option(markup).getOrElse(item.itemType.objectListMarkup)
    "<div class='search-item'>" + MarkupRenderer.render(item, m, sreq) + "</div>"
  }

  private def createSearch(folder: Folder,
                           selections: Iterable[FacetSelection],
                           facets: Iterable[FacetField]): Search = {
    FacetSelections.selections2search(selections, Search(search)).copy(facets = facets)
  }

  private def facetFields: Seq[FacetField] = {
    Option(attributes) match {
      case None => Nil
      case Some(ids) => ids.split(",").filterNot(_.isEmpty).map(id => AttributeFacetField(id.toLong))
    }
  }
}
