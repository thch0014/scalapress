package com.cloudray.scalapress.folder.section

import javax.persistence.{ManyToOne, JoinColumn, Table, Entity}
import com.cloudray.scalapress.section.Section
import com.cloudray.scalapress.theme.{MarkupRenderer, Markup}
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFound, NotFoundAction}
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.framework.ScalapressRequest

/** @author Stephen Samuel */
@Entity
@Table(name = "blocks_subcategories")
class SubfolderSection extends Section {

  @ManyToOne
  @JoinColumn(name = "markup", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var markup: Markup = _

  def render(request: ScalapressRequest): Option[String] = {
    Option(folder).map(folder => {
      val m = Option(markup)
        .orElse(Option(request.folderSettings.subfolderMarkup))
        .getOrElse(SubfolderSection.DefaultMarkup)
      val render = MarkupRenderer.renderFolders(_folders, m, request)
      render
    })
  }

  def _folders: Seq[Folder] = folder.sortedSubfolders.filterNot(_.hidden)
  def desc = "Show a clickable list of the sub folders of this folder"

}

object SubfolderSection {
  val DefaultMarkup = new Markup
  DefaultMarkup.start = "<ul>"
  DefaultMarkup.body = "<li>[folder?link=1]</li>"
  DefaultMarkup.end = "</ul>"
}