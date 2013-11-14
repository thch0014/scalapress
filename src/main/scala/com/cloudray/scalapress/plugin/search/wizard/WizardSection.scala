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
import scala.collection.JavaConverters._

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
    val selections = SearchUrlUtils.facets(sreq)

    val searchService = sreq.context.bean[SearchService]
    val search = createSearch(selections)
    val result = searchService.search(search)

    val items = getItems(result, sreq.context.itemDao)

    // only render one facet at time for wizards, which is the first non-selected single term attribute facet
    val facet = result.facets
      .filter(_.field.isInstanceOf[AttributeFacetField])
      .filterNot(facet => selections.map(_.field).contains(facet.field))
      .find(_.terms.size > 1)

    val step = facet
      .flatMap(f => steps.asScala.find(_.attribute == f.field.asInstanceOf[AttributeFacetField].id))

    val sb = new StringBuilder
    if (facet.isDefined && step.isDefined) sb append renderStep(facet.get, step.get, uri)
    if (selections.size > 0) sb append renderItems(items, sreq)
    sb.mkString
  }

  private def facets: Seq[FacetField] = steps.asScala.map(step => AttributeFacetField(step.attribute))

  private def createSearch(selections: Iterable[FacetSelection]): Search = {
    FacetSelections.selections2search(selections, Search(search)).copy(facets = facets)
  }

  private def getItems(result: SearchResult, dao: ItemDao): Seq[Item] = dao.findBulk(result.refs.map(_.id))

  private def renderStep(facet: Facet, step: WizardStep, uri: Uri): Node = {
    val terms = facet.terms.map(term =>
      Scalate.layout(
        "/com/cloudray/scalapress/search/section/facetterm.ssp",
        Map("count" -> term.count,
          "value" -> term.value,
          "url" -> uri.replaceParams(facet.field.key, term.value).toString())
      )
    )
    <div class="search-wizard-step">
      <h6>
        {step.title}{step.text}
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
case class WizardStep(title: String, text: String, attribute: Long) {
  def this() = this(null, null, 0l)
}