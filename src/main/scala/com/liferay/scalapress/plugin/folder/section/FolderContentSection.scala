package com.liferay.scalapress.plugin.folder.section

import javax.persistence.{Column, Table, Entity}
import com.liferay.scalapress.{Section, ScalapressContext, ScalapressRequest}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_content")
class FolderContentSection extends Section {

    @Column(name = "content", length = 10000)
    @BeanProperty var content: String = _

    def desc = "Edit and then display a section of content when viewing this object"

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {
        Option(content)
          .map(c => {
            c.replace("src=\"/images/", "src=\"" + context.assetStore.cdn + "/")
              .replace("src=\"images/", "src=\"" + context.assetStore.cdn + "/")
        })
    }
}
