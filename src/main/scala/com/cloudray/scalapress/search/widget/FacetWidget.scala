package com.cloudray.scalapress.search.widget

import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.ScalapressRequest
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.{SavedSearch, Facet, SearchService}
import com.cloudray.scalapress.folder.Folder
import javax.persistence.{Column, Transient, Table, Entity}
import com.cloudray.scalapress.util.Scalate
import javax.servlet.http.HttpServletRequest
import scala.beans.BeanProperty

/** @author Stephen Samuel
  *
  *         Shows facets for all the objects in the current folder
  **/
@Entity
@Table(name = "search_widget_facet")
class FacetWidget extends Widget {

  @Transient
  @Autowired var service: SearchService = _

  @Column
  @BeanProperty var attributes: String = _

  def render(sreq: ScalapressRequest): Option[String] = sreq.folder.flatMap(folder => _render(folder, sreq.request))

  def _render(folder: Folder, req: HttpServletRequest): Option[String] = _facets(folder) match {
    case facets if facets.isEmpty => None
    case facets => _renderFacets(facets)
  }

  def _renderFacets(facets: Iterable[Facet]) =
    Some("<div class='facets'>" + facets.map(_renderFacet).mkString("\n") + "</div>")

  def _renderFacet(facet: Facet): String =
    Scalate.layout("/com/cloudray/scalapress/search/widget/facet.ssp", Map("facet" -> facet))

  def _facets(folder: Folder): Iterable[Facet] = {
    _attributes match {
      case seq if seq.isEmpty => Nil
      case seq =>
        val search = _createSearch(folder, seq)
        val result = service.search(search)
        result.facets
    }
  }

  def _attributes: Seq[String] = Option(attributes).getOrElse("").split(",").map(_.trim)

  def _createSearch(folder: Folder, facets: Iterable[String]): SavedSearch = {
    val search = new SavedSearch
    search.searchFolders = folder.id.toString
    search.facets = facets
    search
  }
}
