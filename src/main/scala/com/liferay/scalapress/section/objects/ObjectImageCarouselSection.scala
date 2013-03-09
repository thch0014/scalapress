package com.liferay.scalapress.section.objects

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import javax.persistence.{Table, Entity}
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_objects_images")
class ObjectImageCarouselSection extends Section {

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = None
    def desc: String = ""
}
