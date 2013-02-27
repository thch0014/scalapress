package com.liferay.scalapress.search.section

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import javax.persistence.{OneToOne, ManyToOne, JoinColumn, Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.domain.Markup
import com.liferay.scalapress.service.theme.MarkupRenderer
import com.liferay.scalapress.search.SavedSearch
import com.liferay.scalapress.section.Section

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

    @ManyToOne
    @JoinColumn(name = "markup")
    @BeanProperty var markup: Markup = _

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        Option(search) match {
            case None => None
            case Some(s) =>
                val results = context.searchService.search(search)
                val objs = results.hits().hits().map(arg => context.objectDao.find(arg.id.toLong))
                objs.size match {
                    case 0 => Some("<!-- no search results (search #" + search.id + ") -->")
                    case _ =>
                        Option(markup).orElse(Option(objs.head.objectType.objectListMarkup)) match {
                            case None => Some("<!-- no search results markup -->")
                            case Some(m) =>
                                val rendered = MarkupRenderer.renderObjects(objs, m, request, context)
                                Some(rendered)
                        }
                }
        }
    }

    def desc: String = "Shows results of a predefined search"
    override def backoffice: String = "/backoffice/search/section/savedsearch/" + id
}
