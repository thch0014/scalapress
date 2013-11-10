package com.cloudray.scalapress.search.section

import com.cloudray.scalapress.framework.ScalapressRequest
import javax.persistence._
import scala.beans.BeanProperty
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.theme.{Markup, MarkupRenderer}
import com.cloudray.scalapress.item.{Item, ItemDao}
import scala.xml.Node
import com.github.theon.uri.Uri
import com.cloudray.scalapress.search.AttributeFacetField
import com.cloudray.scalapress.search.Facet
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.util.UrlParser

/** @author Stephen Samuel */
@Entity
@Table(name = "search_section_facet")
class FacetSection extends Section {

  def desc: String = "Facet browser for a folders items"
  override def backoffice: String = "/backoffice/search/section/facet/" + id

  @Column(name = "attributes")
  @BeanProperty
  var attributes: String = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "markup")
  @BeanProperty
  var markup: Markup = _

  def render(sreq: ScalapressRequest): Option[String] = sreq.folder.map(folder => render(folder, sreq))

  private def render(folder: Folder, sreq: ScalapressRequest): String = {

    val uri = UrlParser.parse(sreq)
    val selectedFacets = FacetValueUrlParser.parse(sreq)

    val result = search(folder, sreq, uri)
    val facets = renderFacets(result, uri)
    val items = renderItems(result, sreq)
    facets + "\n" + items
  }

  private def renderSelectedFacets(selected: Seq[FacetValue], uri: Uri): String = {
    "<div class='search-selected-facets'>" +
      selected.map(renderSelectedFacet(_, uri)).mkString("\n") +
      "</div>"
  }

  private def field2name(field: FacetField): String = field match {
    case AttributeFacetField(_) => "Attribute Name Here"
    case TagsFacetField => "Tags"
    case _ => ""
  }

  private def renderSelectedFacet(selected: FacetValue, uri: Uri): String = {
    val removed = uri.removeParams(selected.field.key)
    "<div class='search-selected-facet'>" +
      field2name(selected.field) + ":" + selected.value + " <a href=" + removed + ">x</a>" +
      "</div>"
  }

  private def renderFacets(result: SearchResult, uri: Uri): String = {
    "<div class='search-facets'>" + result
      .facets
      .filterNot(_.terms.isEmpty)
      .map(renderFacet(_, uri))
      .mkString("\n") +
      "</div>"
  }

  private def renderFacet(facet: Facet, uri: Uri): Node = {
    val terms = facet.terms.map(term =>
      <dl>
        <dt>
          <a href={uri.param(facet.field.key -> term.value)}>
            {term.value}
          </a>
        </dt>
        <dd>
          (
          {term.count}
          )
        </dd>
      </dl>)
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

  private def search(folder: Folder, sreq: ScalapressRequest, uri: Uri): SearchResult = {
    val selectedFacets = uri.query.params.map(param => FacetValue(FacetField(param._1).get, param._2.head))
    val searchService = sreq.context.bean[SearchService]
    searchService.search(_createSearch(folder, facetFields, selectedFacets))
  }

  private def _createSearch(folder: Folder,
                            facets: Iterable[FacetField],
                            selectedFacets: Iterable[FacetValue]): Search = {
    val search = Search(folders = List(folder.id.toString))
    //search.facets = facets.filterNot(facet => selectedFacets.exists(_.field == facet))
    //search.selectedFacets = selectedFacets
    search
  }

  private def facetFields: Seq[FacetField] = {
    Option(attributes) match {
      case None => Nil
      case Some(ids) => ids.split(",").filterNot(_.isEmpty).map(id => AttributeFacetField(id.toLong))
    }
  }
}
