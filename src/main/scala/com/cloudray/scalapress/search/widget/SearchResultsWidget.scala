package com.cloudray.scalapress.search.widget

import javax.persistence._
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.widgets.Widget
import com.cloudray.scalapress.search.SavedSearch
import org.hibernate.annotations.{CacheConcurrencyStrategy, Fetch, FetchMode}
import com.cloudray.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty

/** @author Stephen Samuel
  *
  *         Shows the results of a saved search
  *
  * */
@Entity
@Table(name = "boxes_highlighted_items")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class SearchResultsWidget extends Widget {

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
        context.savedSearchDao.save(search)
    }

    def render(request: ScalapressRequest): Option[String] = {
        Option(search) match {
            case None => None
            case Some(s) =>
                val result = request.context.searchService.search(search)
                val objs = request.context.objectDao.findBulk(result.refs.map(_.id)).filter(_.status.equalsIgnoreCase("live"))
                objs.size match {
                    case 0 => Some("<!-- search widget #" + id + ": no results (search #" + search.id + ") -->")
                    case _ =>
                        Option(markup).orElse(Option(objs.head.objectType.objectListMarkup)) match {
                            case None => Some("<!-- search widget #" + id + ": no markup -->")
                            case Some(m) =>
                                val rendered = MarkupRenderer.renderObjects(objs, m, request)
                                Some(rendered)
                        }
                }
        }
    }
}