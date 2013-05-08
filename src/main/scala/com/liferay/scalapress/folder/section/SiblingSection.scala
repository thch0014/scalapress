package com.liferay.scalapress.folder.section

import javax.persistence.{JoinColumn, ManyToOne}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
//@Entity
//@Table(name = "blocks_siblings")
class SiblingSection extends Section {

    @ManyToOne
    @JoinColumn(name = "markup", nullable = true)
    @BeanProperty var markup: Markup = _

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {

        val default = new Markup
        default.start = "<ul>"
        default.body = "<li>[category?link=1]</li>"
        default.end = "</ul>"

        Option(folder.parent) match {
            case None => None
            case Some(parent) =>
                val siblings = parent.subfolders.asScala - folder
                val m = Option(markup)
                  .orElse(Option(context.markupDao.byName("Default siblings markup"))).getOrElse(default)
                val render = MarkupRenderer.renderFolders(siblings.toList, default, request, context)
                Option(render)
        }
    }

    def desc = "Show a clickable list of the sibling folders of this folder"

}
