package com.cloudray.scalapress.folder.widget

import javax.persistence._
import org.hibernate.annotations._
import com.cloudray.scalapress.widgets.Widget
import javax.persistence.Table
import javax.persistence.Entity
import scala.beans.BeanProperty
import com.cloudray.scalapress.folder.Folder
import scala.xml.{Unparsed, Utility, Node}
import scala.collection.mutable.ListBuffer
import com.cloudray.scalapress.framework.{UrlGenerator, Logging, ScalapressRequest}

/** @author Stephen Samuel */
@Table(name = "categories_boxes")
@Entity
class FoldersWidget extends Widget with Logging {

  @BeanProperty var depth: Int = _
  @BeanProperty var excludeCurrent: Boolean = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "root")
  @Fetch(FetchMode.JOIN)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var start: Folder = _

  @BeanProperty
  var exclusions: String = _

  override def backoffice = "/backoffice/plugin/folder/widget/folder/" + id

  override def render(sreq: ScalapressRequest): Option[String] = {
    val root = Option(start).getOrElse(sreq.folderRoot)
    val xml = _renderChildren(root, 1)
    val rendered = Option(xml).map(Utility.trim(_).toString())
    rendered
  }

  def _renderChildren(parent: Folder, level: Int): Node = {
    _folders(parent) match {
      case list if list.isEmpty => null
      case list => _renderChildren(list, level)
    }
  }

  def _renderChildren(children: Seq[Folder], level: Int): Node = {
    val css = if (level == 1) "widget-folder-plugin" else null

    val nodes = new ListBuffer[Node]
    for ( child <- children ) {
      nodes.append(_renderFolder(child, level))
      if (depth > level)
        nodes.append(_renderChildren(child, level + 1))
    }

    <ul class={css}>
      {nodes}
    </ul>
  }

  def _renderFolder(folder: Folder, level: Int): Node = {

    val css = s"l$level"
    val id = s"w${this.id}_f${folder.id}"
    val link = Unparsed(UrlGenerator.link(folder))

    val xml = <li class={css} id={id}>
      {link}
    </li>

    Utility.trim(xml)
  }

  def _folders(parent: Folder): Seq[Folder] =
    parent.sortedSubfolders.toSeq
      .filterNot(_.hidden)
      .filterNot(_.name == null)
      .filterNot(f => _exclusions.contains(f.id.toString))
      .filterNot(f => _exclusions.contains(f.name.toLowerCase.replaceAll("\\s{2,}", " ").trim))

  def _exclusions: Seq[String] =
    Option(exclusions)
      .map(_.toLowerCase)
      .map(_.split(Array('\n', ',')).map(_.toLowerCase.replaceAll("\\s{2,}", " ").trim))
      .getOrElse(Array[String]()).toSeq
}
