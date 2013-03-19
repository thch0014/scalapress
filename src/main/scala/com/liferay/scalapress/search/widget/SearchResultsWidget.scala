package com.liferay.scalapress.search.widget

import javax.persistence.{FetchType, ManyToOne, Table, Entity, JoinColumn, OneToOne}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import reflect.BeanProperty
import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.service.theme.MarkupRenderer
import com.liferay.scalapress.search.SavedSearch
import org.hibernate.annotations.{Fetch, FetchMode}
import com.liferay.scalapress.theme.Markup

/** @author Stephen Samuel
  *
  *         Shows the results of a saved search
  *
  **/
@Entity
@Table(name = "boxes_highlighted_items")
class SearchResultsWidget extends Widget {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "search", nullable = true)
    @Fetch(FetchMode.JOIN)
    @BeanProperty var search: SavedSearch = _

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "markup")
    @Fetch(FetchMode.JOIN)
    @BeanProperty var markup: Markup = _

    override def backoffice = "/backoffice/search/widget/results/" + id

    override def _init(context: ScalapressContext) {
        search = new SavedSearch
        context.savedSearchDao.save(search)
    }

    def render(req: ScalapressRequest): Option[String] = {
        Option(search) match {
            case None => None
            case Some(s) =>
                val results = req.context.searchService.search(search)
                val objs = results.hits().hits().map(arg => req.context.objectDao.find(arg.id.toLong))
                objs.size match {
                    case 0 => Some("<!-- search widget #" + id + ": no results (search #" + search.id + ") -->")
                    case _ =>
                        Option(markup).orElse(Option(objs.head.objectType.objectListMarkup)) match {
                            case None => Some("<!-- search widget #" + id + ": no markup -->")
                            case Some(m) =>
                                val rendered = MarkupRenderer.renderObjects(objs, m, req, req.context)
                                Some(rendered)
                        }
                }
        }
    }
}
