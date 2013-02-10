package com.liferay.scalapress.plugin.search.sections

import com.liferay.scalapress.{Section, ScalapressContext, ScalapressRequest}
import javax.persistence.{OneToOne, Column, ManyToOne, JoinColumn, Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.domain.Markup
import com.liferay.scalapress.plugin.search.SavedSearch
import com.liferay.scalapress.service.theme.MarkupRenderer

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

    @Column(name = "pageSize", nullable = false)
    @BeanProperty var pageSize: Int = 5

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        None
        Option(search) match {
            case None => None
            case Some(s) =>
                val results = context.searchService.search(search, pageSize)
                val objs = results.hits().hits().map(arg => context.objectDao.find(arg.id.toLong))
                val rendered = MarkupRenderer.renderObjects(objs, markup, request, context)
                Some(rendered)
        }
    }

    def desc: String = "Shows results of a predefined search"
    override def backoffice: String = "/backoffice/search/section/savedsearch/" + id
}
