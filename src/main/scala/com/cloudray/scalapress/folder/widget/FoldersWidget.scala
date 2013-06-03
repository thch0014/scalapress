package com.cloudray.scalapress.folder.widget

import javax.persistence._
import com.cloudray.scalapress.{Logging, ScalapressRequest}
import collection.mutable.ArrayBuffer
import org.hibernate.annotations._
import com.cloudray.scalapress.widgets.Widget
import javax.persistence.Table
import javax.persistence.Entity
import scala.beans.BeanProperty
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.util.UrlGenerator

/** @author Stephen Samuel */
@Table(name = "categories_boxes")
@Entity
class FoldersWidget extends Widget with Logging {

    @BeanProperty var depth: Int = _
    @BeanProperty var includeHome: Boolean = _
    @BeanProperty var excludeCurrent: Boolean = _

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "root")
    @Fetch(FetchMode.JOIN)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var start: Folder = _

    @BeanProperty var exclusions: String = _

    override def backoffice = "/backoffice/plugin/folder/widget/folder/" + id

    override def render(req: ScalapressRequest): Option[String] = {
        val buffer = new ArrayBuffer[String]
        val root = Option(start).getOrElse(req.folderRoot)
        _renderFolderLevel(root, 1, buffer)
        Some(buffer.mkString("\n"))
    }

    def _renderFolderLevel(parent: Folder, level: Int, buffer: ArrayBuffer[String]) {

        if (level == 1)
            buffer.append("<ul class=\"widget-folder-plugin\">")
        else
            buffer.append("<ul>")

        val children = _children(parent)

        for ( folder <- children ) {
            buffer.append("<li class=\"l" + level + "\" id=\"w" + id + "_f" + folder.id + "\">")
            buffer.append(UrlGenerator.link(folder))
            if (level < depth)
                _renderFolderLevel(folder, level + 1, buffer)
            buffer.append("</li>")
        }

        buffer.append("</ul>")
    }

    def _children(parent: Folder) =
        parent.sortedSubfolders
          .filterNot(_.hidden)
          .filterNot(_.name == null)
          .filterNot(f => _exclusions.contains(f.id.toString))
          .filterNot(f => _exclusions.contains(f.name.toLowerCase.replaceAll("\\s{2,}", " ").trim))
          .sortBy(_.name)

    def _exclusions: Seq[String] =
        Option(exclusions)
          .map(_.toLowerCase)
          .map(_.split(Array('\n', ',')).map(_.toLowerCase.replaceAll("\\s{2,}", " ").trim))
          .getOrElse(Array[String]()).toSeq
}
