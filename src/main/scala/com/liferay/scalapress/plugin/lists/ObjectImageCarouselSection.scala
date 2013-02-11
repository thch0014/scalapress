package com.liferay.scalapress.plugin.lists

import com.liferay.scalapress.{Section, ScalapressContext, ScalapressRequest}
import javax.persistence.{Table, Entity}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_objects_images")
class ObjectImageCarouselSection extends Section {

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = None
    def desc: String = ""
}