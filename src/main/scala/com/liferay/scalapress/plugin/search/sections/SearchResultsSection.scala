package com.liferay.scalapress.plugin.search.sections

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.section.Section
import javax.persistence.{Column, JoinColumn, OneToOne, Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.plugin.search.SavedSearch

/** @author Stephen Samuel
  *
  *         Shows the results of a saved search
  *
  **/
@Entity
@Table(name = "blocks_highlighted_items")
class SearchResultsSection extends Section {

    @OneToOne
    @JoinColumn(name = "search")
    @BeanProperty var search: SavedSearch = _

    @Column(name = "pageSize", nullable = false)
    @BeanProperty var pageSize: Int = 50

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        Option(search) match {
            case None => None
            case Some(s) =>
                val results = context.searchService.search(search, pageSize)
                Some(results.toString)
        }
    }

    def desc: String = "Shows results of a predefined search"
    override def backoffice: String = "/backoffice/plugin/search/section/savedsearch/" + id
}
