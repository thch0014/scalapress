package com.liferay.scalapress.obj

import javax.persistence.{Table, Entity, Column}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "plugins_objects_content")
class ObjectContentSection extends Section {

    @Column(name = "content")
    var content: String = _

    def desc = "Edit and then display a section of content when viewing this object"

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = Option(content)
}
