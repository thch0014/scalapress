package com.cloudray.scalapress.search.section

import com.cloudray.scalapress.framework.ScalapressRequest
import javax.persistence._
import scala.beans.BeanProperty
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.theme.{Markup, MarkupRenderer}
import com.cloudray.scalapress.item.{Item, ItemDao}
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.search.Facet
import scala.xml.Node

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
    val result = search(folder, sreq)
    val facets = renderFacets(result)
    val items = renderItems(result, sreq)
    facets + "\n" + items
  }

  private def renderFacets(result: SearchResult): String = {
    "<div class='search-facets'>" + result.facets.map(renderFacet).mkString("\n") + "</div>"
  }

  private def renderFacet(facet: Facet): Node = {
    <div class="search-facet">
      <h3>
        {facet.name}
      </h3>
      <dl>
        <dt></dt>
        <dd></dd>
      </dl>
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

  private def search(folder: Folder, sreq: ScalapressRequest): SearchResult = {
    val searchService = sreq.context.bean[SearchService]
    searchService.search(_createSearch(folder, facetFields))
  }

  private def _createSearch(folder: Folder, facets: Iterable[FacetField]): SavedSearch = {
    val search = new SavedSearch
    search.searchFolders = folder.id.toString
    search.facets = facets
    search
  }

  private def facetFields: Seq[FacetField] = {
    Option(attributes) match {
      case None => Nil
      case Some(ids) => ids.split(",").filterNot(_.isEmpty).map(id => AttributeFacetField(id.toLong))
    }
  }
}
