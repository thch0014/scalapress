package com.cloudray.scalapress.search.widget

import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.ScalapressRequest
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.{Facet, SavedSearch, SearchService}
import com.cloudray.scalapress.folder.Folder
import javax.persistence.{Table, Entity}
import com.cloudray.scalapress.util.Scalate

/** @author Stephen Samuel
  *
  *         Shows facets for all the objects in the current folder
  **/
@Entity
@Table(name = "search_widget_facet")
class FacetWidget extends Widget {

  @Autowired var service: SearchService = _

  def render(req: ScalapressRequest): Option[String] = req.folder.flatMap(_render)

  def _render(folder: Folder): Option[String] = _facets(folder) match {
    case facets if facets.isEmpty => None
    case facets => Some("<div class='facets'>" + facets.map(_renderFacet).mkString("\n") + "</div>")
  }

  def _renderFacet(facet: Facet): String =
    Scalate.layout("/com/cloudray/scalapress/search/widget/facet.ssp", Map("facet" -> facet))

  def _facets(folder: Folder): Iterable[Facet] = {
    val search = new SavedSearch
    search.searchFolders = folder.id.toString
    val result = service.search(search)
    result.facets
  }
}
