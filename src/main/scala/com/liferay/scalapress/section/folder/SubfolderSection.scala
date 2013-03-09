package com.liferay.scalapress.section.folder

import javax.persistence.{ManyToOne, JoinColumn, Table, Entity}
import com.liferay.scalapress.domain.Markup
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.service.theme.MarkupRenderer
import scala.collection.JavaConverters._
import reflect.BeanProperty
import com.liferay.scalapress.section.Section

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_subcategories")
class SubfolderSection extends Section {

    //   var selector: Boolean = _
    //   var introText: String = _

    @ManyToOne
    @JoinColumn(name = "markup", nullable = true)
    @BeanProperty var markup: Markup = _

    def render(request: ScalapressRequest, context: ScalapressContext): Option[String] = {

        val default = new Markup
        default.start = "<ul>"
        default.body = "<li>[category?link=1]</li>"
        default.end = "</ul>"


        val m = Option(markup)
          .orElse(Option(context.markupDao.byName("Default subcategories markup"))).getOrElse(default)
        val render = MarkupRenderer.renderFolders(folder.subfolders.asScala.toList, default, request, context)
        Option(render)
    }

    def desc = "Show a clickable list of the sub folders of this folder"

}
