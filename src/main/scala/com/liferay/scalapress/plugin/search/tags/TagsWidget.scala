package com.liferay.scalapress.plugin.search.tags

import com.liferay.scalapress.widgets.Widget
import com.liferay.scalapress.ScalapressRequest
import javax.persistence.{Entity, Table}

/** @author Stephen Samuel */
@Table(name = "plugin_tagswidget")
@Entity
class TagsWidget extends Widget {
    def render(req: ScalapressRequest): Option[String] = {
        None
    }
}
