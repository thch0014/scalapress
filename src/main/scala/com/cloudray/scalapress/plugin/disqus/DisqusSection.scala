package com.cloudray.scalapress.plugin.disqus

import com.cloudray.scalapress.section.Section
import javax.persistence.{Table, Entity}
import scala.beans.BeanProperty
import com.cloudray.scalapress.util.{Scalate}
import com.cloudray.scalapress.framework.{UrlGenerator, ScalapressRequest}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_disqus_section")
class DisqusSection extends Section {

  @BeanProperty
  var shortname: String = _

  override def backoffice: String = "/backoffice/plugin/disqus/section/" + id
  def desc: String = "Disqus commenting system"

  def render(request: ScalapressRequest): Option[String] = {

    val title = request.item.map(_.name).orElse(request.folder.map(_.name)).getOrElse("")
    val url = request.item.map(arg => UrlGenerator.url(arg))
      .orElse(request.folder.map(arg => UrlGenerator.url(arg)))
      .map("http://" + request.installation.domain + _)
      .getOrElse("")

    val normalizedTitle = title.replace("\"", "'")

    Option(shortname)
      .map(arg => Scalate.layout("/com/cloudray/scalapress/plugin/disqus/disqus.ssp",
      Map("shortname" -> shortname, "title" -> normalizedTitle, "id" -> id, "url" -> url, "id" -> this.id.toString)))
  }
}
