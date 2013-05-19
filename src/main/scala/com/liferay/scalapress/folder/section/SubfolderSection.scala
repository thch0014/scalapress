package com.liferay.scalapress.folder.section

import javax.persistence.{ManyToOne, JoinColumn, Table, Entity}
import com.liferay.scalapress.ScalapressRequest
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFound, NotFoundAction}

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_subcategories")
class SubfolderSection extends Section {

    @ManyToOne
    @JoinColumn(name = "markup", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var markup: Markup = _

    def render(request: ScalapressRequest): Option[String] = {

        val default = new Markup
        default.start = "<ul>"
        default.body = "<li>[category?link=1]</li>"
        default.end = "</ul>"

        val m = Option(markup)
          .orElse(Option(request.context.markupDao.byName("Default subcategories markup")))
          .getOrElse(default)
        val render = MarkupRenderer.renderFolders(_folders, default, request)
        Option(render)
    }

    def _folders = folder.sortedSubfolders.filterNot(_.hidden)

    def desc = "Show a clickable list of the sub folders of this folder"

}
