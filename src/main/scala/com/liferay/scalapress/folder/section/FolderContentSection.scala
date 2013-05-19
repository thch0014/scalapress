package com.liferay.scalapress.folder.section

import javax.persistence.{Column, Table, Entity}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.section.Section
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_content")
class FolderContentSection extends Section {

    @Column(name = "content", length = 10000)
    @BeanProperty var content: String = _

    def desc = "Edit and then display a section of content when viewing this object"

    def render(request: ScalapressRequest): Option[String] = {
        Option(content)
          .map(c => {
            c.replace("src=\"/images/", "src=\"" + request.context.assetStore.baseUrl + "/")
              .replace("src=\"images/", "src=\"" + request.context.assetStore.baseUrl + "/")
        })
    }
}
