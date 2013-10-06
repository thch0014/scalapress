package com.cloudray.scalapress.search.widget

import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.ScalapressRequest
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.{SavedSearch, Facet, SearchService}
import com.cloudray.scalapress.folder.Folder
import javax.persistence.{Table, Entity}
import com.cloudray.scalapress.util.Scalate
import scala.collection.JavaConverters._
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel
  *
  *         Shows facets for all the objects in the current folder
  **/
@Entity
@Table(name = "search_widget_facet")
class FacetWidget extends Widget {

  @Autowired var service: SearchService = _

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
    folder.objects.asScala.headOption match {
      case None => Nil
      case Some(obj) =>
        val search = _createSearch(folder, obj)
        val result = service.search(search)
        result.facets
    }
  }

  def _createSearch(folder: Folder, obj: Obj): SavedSearch = {
    _createSearch(folder, obj.objectType.attributes.asScala.filter(_.facet).map(_.id.toString))
  }

  def _createSearch(folder: Folder, facets: Iterable[String]): SavedSearch = {
    val search = new SavedSearch
    search.searchFolders = folder.id.toString
    search.facets = facets
    search
  }
}
