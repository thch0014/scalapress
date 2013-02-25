package com.liferay.scalapress.plugin.search.widget

import javax.persistence.{OneToOne, JoinColumn, Entity, Table}
import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import reflect.BeanProperty
import com.liferay.scalapress.search.{SearchFormRenderer, SearchForm}

/** @author Stephen Samuel
  *
  *         Shows a search box with options
  *
  * */
@Entity
@Table(name = "boxes_search")
class SearchFormWidget extends Widget {

    @OneToOne
    @JoinColumn(name = "searchForm")
    @BeanProperty var searchForm: SearchForm = _

    def render(req: ScalapressRequest): Option[String] = None

    override def backoffice = "/backoffice/plugin/search/widget/searchform/" + id
}
