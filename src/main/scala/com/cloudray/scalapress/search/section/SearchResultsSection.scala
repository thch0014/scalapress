package com.cloudray.scalapress.search.section

import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.persistence._
import com.cloudray.scalapress.search.SavedSearch
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty
import scala.Some

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

    override def _init(context: ScalapressContext) {
        search = new SavedSearch
        context.savedSearchDao.save(search)
    }

    def render(request: ScalapressRequest): Option[String] = {
        Option(search) match {
            case None => None
            case Some(s) =>
                val result = request.context.searchService.search(search)
                val objs = request.context.objectDao.findBulk(result.refs.map(_.id)).filter(_.status.equalsIgnoreCase("live"))
                objs.size match {
                    case 0 => Some("<!-- no search results (search #" + search.id + ") -->")
                    case _ =>
                        Option(markup).orElse(Option(objs.head.objectType.objectListMarkup)) match {
                            case None => Some("<!-- no search results markup -->")
                            case Some(m) =>
                                val rendered = MarkupRenderer.renderObjects(objs, m, request)
                                Some(rendered)
                        }
                }
        }
    }

    def desc: String = "Shows results of a predefined search"
    override def backoffice: String = "/backoffice/search/section/savedsearch/" + id
}
