package com.liferay.scalapress.search.section

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest, Section}
import javax.persistence.{JoinColumn, ManyToOne, Table, Entity}
import reflect.BeanProperty
import com.liferay.scalapress.search.{SearchFormRenderer, SearchForm}

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_search")
class SearchFormSection extends Section {

    @ManyToOne
    @JoinColumn(name = "search_form")
    @BeanProperty var searchForm: SearchForm = _

    def desc: String = "Search form"

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = Option(searchForm) match {
        case None => None
        case Some(form) =>
            val rendered = SearchFormRenderer.render(form)
            Some(rendered)
    }

}
