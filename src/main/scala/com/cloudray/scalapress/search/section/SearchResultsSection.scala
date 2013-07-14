package com.cloudray.scalapress.search.section

import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.persistence._
import com.cloudray.scalapress.search.{ObjectRef, SavedSearch}
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty
import scala._
import scala.Some
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel
  *
  *         Shows the results of a saved search
  *
  * */
@Entity
@Table(name = "blocks_highlighted_items")
class SearchResultsSection extends Section {

  @OneToOne
  @JoinColumn(name = "search")
  @BeanProperty var search: SavedSearch = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "markup")
  @BeanProperty var markup: Markup = _

  def desc: String = "Shows results of a predefined search"
  override def backoffice: String = "/backoffice/search/section/savedsearch/" + id

  override def _init(context: ScalapressContext) {
    search = new SavedSearch
    search.maxResults = 20
    context.savedSearchDao.save(search)
  }

  def render(request: ScalapressRequest): Option[String] = {
    Option(search) match {
      case None => Some("<!-- no search object set (section #" + id + ") -->")
      case Some(s) =>
        val objects = _objects(request)
        objects.size match {
          case 0 => Some("<!-- no search results (search #" + search.id + ") -->")
          case _ =>
            Option(markup).orElse(Option(objects.head.objectType.objectListMarkup)) match {
              case None => Some("<!-- no search results markup -->")
              case Some(m) =>
                val rendered = MarkupRenderer.renderObjects(objects, m, request)
                Some(rendered)
            }
        }
    }
  }

  def _objects(request: ScalapressRequest): Seq[Obj] = {
    val result = request.context.searchService.search(search)
    val objs = request.context.objectDao
      .findBulk(result.refs.map(_.id))
      .filter(obj => Obj.STATUS_LIVE.equalsIgnoreCase(obj.status))
    _reorder(result.refs, objs)
  }

  def _reorder(refs: Seq[ObjectRef], objs: Seq[Obj]): Seq[Obj] = {
    val ids = refs.map(_.id)
    objs.sortWith((a, b) => ids.indexOf(a.id) < ids.indexOf(b.id))
  }

}
