package com.liferay.scalapress.folder.section

import javax.persistence.{ManyToOne, JoinColumn, Table, Entity}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.FolderOrdering
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_subcategories")
class SubfolderSection extends Section {

    @ManyToOne
    @JoinColumn(name = "markup", nullable = true)
    @BeanProperty var markup: Markup = _

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {

        val default = new Markup
        default.start = "<ul>"
        default.body = "<li>[category?link=1]</li>"
        default.end = "</ul>"

        val m = Option(markup)
          .orElse(Option(context.markupDao.byName("Default subcategories markup")))
          .getOrElse(default)
        val render = MarkupRenderer.renderFolders(_folders, default, request, context)
        Option(render)
    }

    def _folders = {
        val subfolders = folder.subfolders.asScala.toSeq.filterNot(_.hidden)
        val sorted = folder.folderOrdering match {
            case FolderOrdering.Alphabetical => subfolders.sortBy(f => Option(f.name).getOrElse(""))
            case _ => subfolders.sortBy(_.position)
        }
        sorted
    }

    def desc = "Show a clickable list of the sub folders of this folder"

}
