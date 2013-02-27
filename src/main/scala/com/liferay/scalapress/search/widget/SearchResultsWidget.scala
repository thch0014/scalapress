package com.liferay.scalapress.search.widget

import javax.persistence.{Table, Entity, JoinColumn, OneToOne}
import com.liferay.scalapress.ScalapressRequest
import reflect.BeanProperty
import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.plugin.search.SavedSearch

/** @author Stephen Samuel
  *
  *         Shows the results of a saved search
  *
  **/
@Entity
@Table(name = "boxes_highlighted_items")
class SearchResultsWidget extends Widget {

    @OneToOne
    @JoinColumn(name = "search", nullable = true)
    @BeanProperty var search: SavedSearch = _

    def render(req: ScalapressRequest): Option[String] = {
        None
    }
}
