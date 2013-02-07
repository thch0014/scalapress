package com.liferay.scalapress.plugin.folder

import javax.persistence.{Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.{Logging, ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.domain.Folder
import scala.collection.JavaConverters._
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.service.FriendlyUrlGenerator
import com.liferay.scalapress.widgets.Widget

/** @author Stephen Samuel */
@Table(name = "categories_boxes")
@Entity
class FoldersWidget extends Widget with Logging {

    override def render(req: ScalapressRequest, context: ScalapressContext): Option[String] = {

        val folders = subfolders(context.folderDao.root, depth).filter(includeHome || _.parent != null)
        logger.debug("Rendering folders widget for {} folders", folders.size)

        val buffer = new ArrayBuffer[String]
        renderFolderLevel(context.folderDao.root, 1, buffer)
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
            if (level < 2)
                renderFolderLevel(folder, level + 1, buffer)
            buffer.append("</li>")
        }
        buffer.append("</ul>")
    }

    private def subfolders(folder: Folder, depth: Int): List[Folder] = depth match {
        case 0 => List(folder)
        case _ => folder.subfolders.asScala.flatMap(subfolders(_, depth - 1)).toList
    }

    @BeanProperty var depth: Int = _
    @BeanProperty var includeHome: Boolean = _
    @BeanProperty var excludeCurrent: Boolean = _

}
