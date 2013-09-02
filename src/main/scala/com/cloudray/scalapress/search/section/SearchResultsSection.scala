package com.cloudray.scalapress.search.section

import com.cloudray.scalapress.{Logging, ScalapressContext, ScalapressRequest}
import javax.persistence._
import com.cloudray.scalapress.search.SavedSearch
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
  **/
@Entity
@Table(name = "blocks_highlighted_items")
class SearchResultsSection extends Section with Logging with SingleObjectCache[Seq[Obj]] {

  val FIVE_MINUTES_MS = 1000 * 60 * 5
  val CacheTimeout = FIVE_MINUTES_MS

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

        val objects = cachedOrExecute(_objects(request))
        logger.debug("...objects loaded [section={}]", id)

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
    if (result.refs.isEmpty) Nil
    else {
      val ids = result.refs.map(_.id)
      val objs = request.context.objectDao
        .findBulk(ids)
        .filter(obj => Obj.STATUS_LIVE.equalsIgnoreCase(obj.status))
      _reorder(ids, objs)
    }
  }

  def _reorder(ids: Seq[Long], objs: Seq[Obj]): Seq[Obj] = {
    objs.sortWith((a, b) => ids.indexOf(a.id) < ids.indexOf(b.id))
  }
}
