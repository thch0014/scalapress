package com.cloudray.scalapress.plugin.search.wizard

import com.cloudray.scalapress.framework.ScalapressRequest
import javax.persistence._
import scala.beans.BeanProperty
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.theme.MarkupRenderer
import com.cloudray.scalapress.item.{Item, ItemDao}
import scala.xml.{Unparsed, Node}
import com.github.theon.uri.Uri
import com.cloudray.scalapress.util.{Scalate, UrlParser}
import com.cloudray.scalapress.search.section.SearchResultsSection
import com.cloudray.scalapress.search.Facet
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.search.FacetSelection
import java.util

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_search_wizard_section")
class WizardSection extends SearchResultsSection {

  override def desc: String = "Wizard based search results section"
  override def backoffice: String = "/backoffice/plugin/search/section/wizard/" + id

  @CollectionTable(name = "plugin_search_wizard_section_steps")
  @ElementCollection(fetch = FetchType.EAGER)
  @BeanProperty
  var steps: java.util.List[WizardStep] = new util.ArrayList[WizardStep]()

  override def render(sreq: ScalapressRequest): Option[String] = sreq.folder.map(folder => render(folder, sreq))

  private def render(folder: Folder, sreq: ScalapressRequest): String = {

    val uri = UrlParser.parse(sreq)
    val selections = FacetSelectionParser.parse(sreq)

    val searchService = sreq.context.bean[SearchService]
    val search = createSearch
    val result = searchService.search(search)

    val items = getItems(result, sreq.context.itemDao)
    val facetsWithMultipleTerms = result.facets.filter(_.terms.size > 1)

    renderSelectedFacets(selections, uri) +
      renderFacets(facetsWithMultipleTerms, uri) +
      renderItems(items, sreq)
  }

  def createSearch = Search.empty

  def getItems(result: SearchResult, dao: ItemDao): Seq[Item] = dao.findBulk(result.refs.map(_.id))

  private def createSearch(folder: Folder,
                           selections: Iterable[FacetSelection],
                           facets: Iterable[FacetField]): Search = {
    FacetSelections.selections2search(selections, Search(search)).copy(facets = facets)
  }

  private def renderSelectedFacets(selected: Seq[FacetSelection], uri: Uri): String = {
    "<div class='search-selected-facets'>" + selected.map(renderSelectedFacet(_, uri)).mkString("\n") + "</div>"
  }

  private def renderSelectedFacet(selected: FacetSelection, uri: Uri): String = {
    val urlWithFacetRemoved = uri.removeParams(selected.field.key)
    "<div class='search-selected-facet'>" + selected.value + " <a href=\"" + urlWithFacetRemoved + "\">x</a>" + "</div>"
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
          "url" -> uri.replaceParams(facet.field.key, term.value).toString())
      )
    )
    <div class="search-facet">
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

@Embeddable
case class WizardStep(title: String, text: String, attribute: String) {
  def this() = this(null, null, null)
}