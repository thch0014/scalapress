package com.liferay.scalapress.widgets

import javax.persistence.{Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.{Logging, ScalapressRequest}
import com.liferay.scalapress.domain.Folder
import scala.collection.JavaConverters._
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.service.FriendlyUrlGenerator

/** @author Stephen Samuel */
@Table(name = "categories_boxes")
@Entity
class FoldersWidget extends Widget with Logging {

    @BeanProperty var depth: Int = _
    @BeanProperty var includeHome: Boolean = _
    @BeanProperty var excludeCurrent: Boolean = _
    @BeanProperty var excludeCategories: String = _

    override def backoffice = "/backoffice/plugin/folder/widget/folder/" + id

    override def render(req: ScalapressRequest): Option[String] = {
        val buffer = new ArrayBuffer[String]
        renderFolderLevel(req.context.folderDao.root, 1, buffer)
        Some(buffer.mkString("\n"))
    }

    private def renderFolderLevel(parent: Folder, level: Int, buffer: ArrayBuffer[String]) {

        if (level == 1)
            buffer.append("<ul class='widget-folder-plugin'>")
        else
            buffer.append("<ul>")

        for (folder <- parent.subfolders.asScala.filterNot(_.hidden)) {
            buffer.append("<li id='w" + id + "_f" + folder.id + "' class='l" + level + "'>")
            buffer.append(FriendlyUrlGenerator.friendlyLink(folder))
            if (level < depth)
                renderFolderLevel(folder, level + 1, buffer)
            buffer.append("</li>")
        }

        buffer.append("</ul>")
    }

}
