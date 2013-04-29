package com.liferay.scalapress.folder

import javax.persistence._
import reflect.BeanProperty
import com.liferay.scalapress.{FriendlyUrlGenerator, Logging, ScalapressRequest}
import scala.collection.JavaConverters._
import collection.mutable.ArrayBuffer
import org.hibernate.annotations.{CacheConcurrencyStrategy, FetchMode, Fetch}
import com.liferay.scalapress.widgets.Widget
import scala.Some

/** @author Stephen Samuel */
@Table(name = "categories_boxes")
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class FoldersWidget extends Widget with Logging {

    @BeanProperty var depth: Int = _
    @BeanProperty var includeHome: Boolean = _
    @BeanProperty var excludeCurrent: Boolean = _

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "root")
    @Fetch(FetchMode.JOIN)
    @BeanProperty var start: Folder = _

    @BeanProperty var exclusions: String = _

    override def backoffice = "/backoffice/plugin/folder/widget/folder/" + id

    override def render(req: ScalapressRequest): Option[String] = {
        val buffer = new ArrayBuffer[String]
        val root = Option(start).getOrElse(req.context.folderDao.root)
        _renderFolderLevel(root, 1, buffer)
        Some(buffer.mkString("\n"))
    }

    def _renderFolderLevel(parent: Folder, level: Int, buffer: ArrayBuffer[String]) {

        if (level == 1)
            buffer.append("<ul class=\"widget-folder-plugin\">")
        else
            buffer.append("<ul>")

        val excluded = Option(exclusions)
          .map(_.toLowerCase)
          .map(_.split("\n").flatMap(_.split(",")))
          .getOrElse(Array[String]()).map(_.trim)

        val children = parent
          .subfolders
          .asScala
          .toSeq
          .filterNot(_.hidden)
          .filterNot(f => excluded.contains(f.id.toString))
          .filterNot(f => excluded.contains(f.name.toLowerCase.trim))
          .filterNot(_.name == null)
          .sortBy(_.name)

        for (folder <- children) {
            buffer.append("<li class=\"l" + level + "\" id=\"w" + id + "_f" + folder.id + "\">")
            buffer.append(FriendlyUrlGenerator.friendlyLink(folder))
            if (level < depth)
                _renderFolderLevel(folder, level + 1, buffer)
            buffer.append("</li>")
        }

        buffer.append("</ul>")
    }

}
