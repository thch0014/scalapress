package com.cloudray.scalapress.search.section

import javax.persistence._
import com.cloudray.scalapress.search.{Search, SavedSearch}
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty
import scala._
import scala.Some
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.framework.{Logging, ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel
  *         Shows the results of a saved search
  * */
@Entity
@Table(name = "blocks_highlighted_items")
class SearchResultsSection extends Section with Logging {

  @OneToOne
  @JoinColumn(name = "search")
  @BeanProperty
  var search: SavedSearch = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "markup")
  @BeanProperty
  var markup: Markup = _

  override def desc: String = "Shows results of a predefined search"
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
        logger.debug("...objects loaded [section={}]", id)

        objects.size match {
          case 0 => Some("<!-- no search results (search #" + search.id + ") -->")
          case _ =>
            Option(markup).orElse(Option(objects.head.itemType.objectListMarkup)) match {
              case None => Some("<!-- no search results markup -->")
              case Some(m) =>
                Some(
                  SearchResultsSection.CONTAINER_START +
                    MarkupRenderer.renderObjects(objects, m, request) +
                    SearchResultsSection.CONTAINER_END
                )
            }
        }
    }
  }

  def _objects(request: ScalapressRequest): Seq[Item] = {
    val result = request.context.searchService.search(Search(search))
    if (result.refs.isEmpty) Nil
    else {
      val ids = result.refs.map(_.id)
      val objs = request.context.itemDao
        .findBulk(ids)
        .filter(obj => Item.STATUS_LIVE.equalsIgnoreCase(obj.status))
      _reorder(ids, objs)
    }
  }

  def _reorder(ids: Seq[Long], objs: Seq[Item]): Seq[Item] = {
    objs.sortWith((a, b) => ids.indexOf(a.id) < ids.indexOf(b.id))
  }
}

object SearchResultsSection {
  val CONTAINER_START = "<div class=\"search-results-section\">"
  val CONTAINER_END = "</div>"
}