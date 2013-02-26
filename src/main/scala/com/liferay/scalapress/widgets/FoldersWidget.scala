package com.liferay.scalapress.widgets

import javax.persistence.{ManyToOne, JoinColumn, Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.{Logging, ScalapressRequest}
import com.liferay.scalapress.domain.Folder
import scala.collection.JavaConverters._
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.service.FriendlyUrlGenerator
import org.hibernate.annotations.CacheConcurrencyStrategy

/** @author Stephen Samuel */
@Table(name = "categories_boxes")
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
class FoldersWidget extends Widget with Logging {

    @BeanProperty var depth: Int = _
    @BeanProperty var includeHome: Boolean = _
    @BeanProperty var excludeCurrent: Boolean = _

    @ManyToOne
    @JoinColumn(name = "root")
    @BeanProperty var start: Folder = _

    @BeanProperty var exclusions: String = _

    override def backoffice = "/backoffice/plugin/folder/widget/folder/" + id

    override def render(req: ScalapressRequest): Option[String] = {
        val buffer = new ArrayBuffer[String]
        val root = Option(start).getOrElse(req.context.folderDao.root)
        renderFolderLevel(root, 1, buffer)
        Some(buffer.mkString("\n"))
    }

    private def renderFolderLevel(parent: Folder, level: Int, buffer: ArrayBuffer[String]) {

        if (level == 1)
            buffer.append("<ul class='widget-folder-plugin'>")
        else
            buffer.append("<ul>")

        val excluded = Option(exclusions)
          .map(_.toLowerCase)
          .map(_.split("\n").flatMap(_.split(",")))
          .getOrElse(Array[String]()).map(_.trim)

        val children = parent
          .subfolders
          .asScala
          .filterNot(_.hidden)
          .filterNot(f => excluded.contains(f.id.toString))
          .filterNot(f => excluded.contains(f.name.toLowerCase.trim))

        for (folder <- children) {
            buffer.append("<li id='w" + id + "_f" + folder.id + "' class='l" + level + "'>")
            buffer.append(FriendlyUrlGenerator.friendlyLink(folder))
            if (level < depth)
                renderFolderLevel(folder, level + 1, buffer)
            buffer.append("</li>")
        }

        buffer.append("</ul>")
    }

}
