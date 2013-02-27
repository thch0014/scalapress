package com.liferay.scalapress.search.widget

import javax.persistence.{ManyToOne, Table, Entity, JoinColumn, OneToOne}
import com.liferay.scalapress.ScalapressRequest
import reflect.BeanProperty
import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.service.theme.MarkupRenderer
import com.liferay.scalapress.domain.Markup
import com.liferay.scalapress.search.SavedSearch

/** @author Stephen Samuel
  *
  *         Shows the results of a saved search
  *
  * */
@Entity
@Table(name = "boxes_highlighted_items")
class SearchResultsWidget extends Widget {

    @OneToOne
    @JoinColumn(name = "search", nullable = true)
    @BeanProperty var search: SavedSearch = _

    @ManyToOne
    @JoinColumn(name = "markup")
    @BeanProperty var markup: Markup = _

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
