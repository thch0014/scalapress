package com.liferay.scalapress.search.section

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import javax.persistence.{Column, JoinColumn, ManyToOne, Table, Entity}
import com.liferay.scalapress.search.{SearchFormRenderer, SearchForm}
import com.liferay.scalapress.section.Section
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_search")
class SearchFormSection extends Section {

    @ManyToOne
    @JoinColumn(name = "search_form")
    @BeanProperty var searchForm: SearchForm = _

    @Column(name = "no_results_text", length = 10000)
    @BeanProperty var noResultsText: String = _

    def desc: String = "Search form"

    override def backoffice: String = "/backoffice/search/section/form/" + id

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = Option(searchForm) match {
        case None => Some("<!--no search form set-->")
        case Some(form) =>
            val rendered = SearchFormRenderer.render(form, this)
            Some(rendered)
    }
    override def _init(context: ScalapressContext) {
        searchForm = new SearchForm
        context.searchFormDao.save(searchForm)
    }
}
