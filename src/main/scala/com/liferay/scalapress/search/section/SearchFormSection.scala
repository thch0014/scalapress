package com.liferay.scalapress.search.section

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import javax.persistence.{JoinColumn, ManyToOne, Table, Entity}
import reflect.BeanProperty
import com.liferay.scalapress.search.{SearchFormRenderer, SearchForm}
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_search")
class SearchFormSection extends Section {

    @ManyToOne
    @JoinColumn(name = "search_form")
    @BeanProperty var searchForm: SearchForm = _

    @BeanProperty var noResultsText: String = _

    def desc: String = "Search form"

    override def backoffice: String = "/backoffice/search/section/form/" + id

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = Option(searchForm) match {
        case None => Some("<!--no search form set-->")
        case Some(form) =>
            val rendered = SearchFormRenderer.render(form)
            Some(rendered)
    }

}
