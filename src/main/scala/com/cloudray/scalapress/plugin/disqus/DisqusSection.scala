package com.cloudray.scalapress.plugin.disqus

import com.cloudray.scalapress.section.Section
import javax.persistence.{Transient, Table, Entity}
import com.cloudray.scalapress.ScalapressRequest
import scala.beans.BeanProperty
import com.cloudray.scalapress.util.UrlGenerator
import org.fusesource.scalate.TemplateEngine

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_disqus_section")
class DisqusSection extends Section {

    @BeanProperty var shortname: String = _

    override def backoffice: String = "/backoffice/plugin/disqus/section/" + id
    def desc: String = "Disqus commenting system"

    def render(request: ScalapressRequest): Option[String] = {

        val title = request.obj.map(_.name).orElse(request.folder.map(_.name)).getOrElse("")
        val url = request.obj.map(arg => UrlGenerator.url(arg))
          .orElse(request.folder.map(arg => UrlGenerator.url(arg)))
          .map("http://" + request.installation.domain + _)
          .getOrElse("")

        Option(shortname)
          .map(arg => engine.layout("/com/cloudray/scalapress/plugin/disqus/disqus.ssp",
            Map("shortname" -> shortname, "title" -> title, "id" -> id, "url" -> url, "id" -> this.id.toString)))
    }
}
