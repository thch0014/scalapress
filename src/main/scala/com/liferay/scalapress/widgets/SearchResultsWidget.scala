package com.liferay.scalapress.widgets

import javax.persistence.{JoinColumn, OneToOne}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import reflect.BeanProperty
import com.liferay.scalapress.domain.SavedSearch

/** @author Stephen Samuel
  *
  *         Shows the results of a saved search
  *
  * */
//@Entity
//@Table(name = "boxes_highlighted_items")
class SearchResultsWidget extends Widget {

    @OneToOne //(cascade = Array(CascadeType.ALL))
    @JoinColumn(name = "savedSearch", nullable = true)
    @BeanProperty var savedSearch: SavedSearch = _

    def render(req: ScalapressRequest, context: ScalapressContext): Option[String] = {
        None
    }
}
