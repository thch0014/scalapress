package com.cloudray.scalapress.search.widget

import javax.persistence._
import com.cloudray.scalapress.{Logging, ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.search.{ObjectRef, SavedSearch}
import org.hibernate.annotations.{Fetch, FetchMode}
import com.cloudray.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.search.section.SingleObjectCache

/** @author Stephen Samuel
  *
  *         Shows the results of a saved search
  *
  * */
@Entity
@Table(name = "boxes_highlighted_items")
class SearchResultsWidget extends Widget with Logging with SingleObjectCache[Seq[Obj]] {

  val CacheTimeout = 1000 * 60

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "search", nullable = true)
  @Fetch(FetchMode.JOIN)
  @BeanProperty var search: SavedSearch = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "markup")
  @Fetch(FetchMode.SELECT)
  @BeanProperty var markup: Markup = _

  override def backoffice = "/backoffice/search/widget/results/" + id

  override def _init(context: ScalapressContext) {
    search = new SavedSearch
    search.maxResults = 20
    context.savedSearchDao.save(search)
  }

  def render(request: ScalapressRequest): Option[String] = {

    Option(search) match {
      case None => None
      case Some(s) =>

        val objects = cachedOrExecute(_objects(request))
        logger.debug("...objects loaded [widget={}]", id)

        if (objects.isEmpty)
          Some("<!-- search widget #" + id + ": no results (search #" + search.id + ") -->")

        else {
          Option(markup).orElse(Option(objects.head.objectType.objectListMarkup)) match {
            case None =>
              Some("<!-- search widget #" + id + ": no markup -->")
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
      val objs = request.context.objectDao
        .findBulk(result.refs.map(_.id))
        .filter(obj => Obj.STATUS_LIVE.equalsIgnoreCase(obj.status))
      _reorder(result.refs, objs)
    }
  }

  def _reorder(refs: Seq[ObjectRef], objs: Seq[Obj]): Seq[Obj] = {
    val ids = refs.map(_.id)
    objs.sortWith((a, b) => ids.indexOf(a.id) < ids.indexOf(b.id))
  }
}