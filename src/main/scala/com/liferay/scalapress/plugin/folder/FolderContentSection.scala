package com.liferay.scalapress.plugin.folder

import javax.persistence.{Column, Table, Entity}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.section.Section
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_content")
class FolderContentSection extends Section {

    @Column(name = "content")
    @BeanProperty var content: String = _

    def desc = "Edit and then display a section of content when viewing this object"

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        Option(content).map("src=\"images/".r.replaceAllIn(_, "src=\"/images/"))
    }
}
