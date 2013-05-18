package com.liferay.scalapress.plugin.search.tags

import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.ScalapressRequest
import javax.persistence.{Entity, Table}
import com.liferay.scalapress.search.SavedSearch

/** @author Stephen Samuel */
@Table(name = "plugin_tagswidget")
@Entity
class TagsWidget extends Widget {
    def render(req: ScalapressRequest): Option[String] = {
        val search = new SavedSearch
        search.facets = Seq("tags")
        val result = req.context.searchService.search(search)
        None
    }
}
