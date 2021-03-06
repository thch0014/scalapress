package com.cloudray.scalapress.plugin.search.facet

import com.cloudray.scalapress.framework.{ScalapressContext, ScalapressRequest}
import javax.persistence._
import scala.beans.BeanProperty
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.theme.MarkupRenderer
import com.cloudray.scalapress.item.{ItemBulkLoader, Item}
import scala.xml.{Unparsed, Node}
import com.github.theon.uri.Uri
import com.cloudray.scalapress.util._
import com.cloudray.scalapress.search.section.SearchResultsSection
import com.cloudray.scalapress.search.AttributeFacetField
import com.cloudray.scalapress.search.Facet
import scala.Some
import com.cloudray.scalapress.search.FacetSelection

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_search_facet_section")
class FacetSection extends SearchResultsSection {

  override def desc: String = "Facet based search section"
  override def backoffice: String = "/backoffice/plugin/search/section/facet/" + id

  @Column(name = "attributes")
  @BeanProperty
  var attributes: String = _

  override def render(sreq: ScalapressRequest): Option[String] = sreq.folder.map(folder => render(folder, sreq))

  private def render(folder: Folder, sreq: ScalapressRequest): String = {

    val uri = UrlParser(sreq)
    val selections = SearchUrlUtils.facets(sreq)
    val sort = SearchUrlUtils.sort(sreq)
    val page = PageUrlUtils.parse(sreq)
    val search = createSearch(folder, selections, facetFields).copy(sort = sort, ignorePriority = true, page = page)

    val searchService = sreq.context.bean[SearchService]
    val searchResult = searchService.search(search)

    val items = new ItemBulkLoader(sreq.context.itemDao).loadFromRefs(searchResult.refs)
    val facetsWithTerms = searchResult.facets.filter(_.terms.size > 0)

    ResultsBar.render(searchResult, sreq) +
      renderSelectedFacets(selections, uri, sreq.context) +
      (if (facetsWithTerms.isEmpty) "" else renderFacets(facetsWithTerms, uri)) +
      renderItems(items, sreq)
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

  private def renderSelectedFacets(selected: Seq[FacetSelection], uri: Uri, context: ScalapressContext): String = {
    "<div class='search-selected-facets clearfix'>" + selected
      .map(renderSelectedFacet(_, uri, context))
      .mkString("\n") + "</div>"
  }

  private def renderSelectedFacet(selected: FacetSelection, uri: Uri, context: ScalapressContext): String = {
    val urlWithFacetRemoved = uri.removeParams(selected.field.key)
    val name = selected.field match {
      case TagsFacetField => "Tag"
      case AttributeFacetField(i) => context.attributeDao.find(i).name
    }
    "<div class='search-selected-facet'>" +
      name + ": " + selected.value + " <a href=\"" + urlWithFacetRemoved + "\">x</a>" +
      "</div>"
  }

  private def renderFacets(facets: Iterable[Facet], uri: Uri): String = {
    "<div class='search-facets'>" + facets.map(renderFacet(_, uri)).mkString("\n") + "</div>"
  }

  private def renderFacet(facet: Facet, uri: Uri): Node = {
    val terms = facet.terms.map(term =>
      Scalate.layout(
        "/com/cloudray/scalapress/search/section/facetterm.ssp",
        Map("count" -> term.count,
          "value" -> term.value,
          "url" -> uri.replaceParams(facet.field.key, term.value).replaceParams("pageNumber", 1).toString())
      )
    )
    <div class="search-facet clearfix">
      <h6>
        {facet.name}
      </h6>{Unparsed(terms.mkString("\n"))}
    </div>
  }

  private def renderItems(items: Seq[Item], sreq: ScalapressRequest): String = {
    "<div class='search-items'>" + items.map(renderItem(_, sreq)).mkString("\n") + "</div>"
  }

  private def renderItem(item: Item, sreq: ScalapressRequest) = {
    val m = Option(markup).getOrElse(item.itemType.objectListMarkup)
    "<div class='search-item'>" + MarkupRenderer.render(item, m, sreq) + "</div>"
  }
}
