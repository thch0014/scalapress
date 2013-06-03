package com.cloudray.scalapress.folder.widget

import javax.persistence._
import com.cloudray.scalapress.{Logging, ScalapressRequest}
import org.hibernate.annotations._
import com.cloudray.scalapress.widgets.Widget
import javax.persistence.Table
import javax.persistence.Entity
import scala.beans.BeanProperty
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.util.UrlGenerator
import scala.xml.{Unparsed, Utility, Node}

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
        val root = Option(start).getOrElse(req.folderRoot)
        val xml = _renderFolderLevel(root, 1)
        Some(Utility.trim(xml).toString())
    }

    def _renderFolderLevel(parent: Folder, level: Int): Node = {

        val css = if (level == 1) "widget-folder-plugin" else null
        val children = _children(parent).map(child => _renderFolder(child, level))

        <ul class={css}>
            {children}
        </ul>
    }

    def _renderFolder(folder: Folder, level: Int): Node = {

        val css = "l" + level
        val id = "w" + this.id + "_f" + folder.id
        val a = Unparsed(UrlGenerator.link(folder))

        val xml = <li class={css} id={id}>
            {a}{if (level < depth)
                _renderFolderLevel(folder, level + 1)}
        </li>

        Utility.trim(xml)
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
