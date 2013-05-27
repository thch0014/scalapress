package com.liferay.scalapress.folder.section

import javax.persistence.{Column, Table, Entity}
import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.section.Section
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_content")
class FolderContentSection extends Section {

    @Column(name = "content", length = 100000)
    @BeanProperty var content: String = _

    override def desc = "Edit and then display a section of content when viewing this object"
    override def backoffice = "backoffice/folder/section/content/" + id

    def render(request: ScalapressRequest): Option[String] = {
        Option(content)
          .map(c => {
            c.replace("src=\"/images/", "src=\"" + request.context.assetStore.baseUrl + "/")
              .replace("src=\"images/", "src=\"" + request.context.assetStore.baseUrl + "/")
        })
    }
}
