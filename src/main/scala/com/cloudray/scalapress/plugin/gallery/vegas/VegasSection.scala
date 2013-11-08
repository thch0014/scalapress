package com.cloudray.scalapress.plugin.gallery.vegas

import javax.persistence._
import com.cloudray.scalapress.section.Section
import scala.collection.JavaConverters._
import com.cloudray.scalapress.framework.ScalapressRequest
import com.cloudray.scalapress.plugin.gallery.base.{Gallery, Image}
import scala.Some
import org.hibernate.annotations.{NotFoundAction, NotFound}
import scala.beans.BeanProperty
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_gallery_vegas")
class VegasSection extends Section {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gallery")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var gallery: Gallery = _

  override def desc: String = "Showing a gallery using the 'Vegas Plugin' (" + Option(gallery)
    .map(_.name)
    .getOrElse("-No Gallery Set-") + ")"
  override def backoffice: String = "/backoffice/plugin/gallery/vegas/section/" + id

  override def render(request: ScalapressRequest): Option[String] = {
    Some("<script>" + VegasSection.js + "</script>")
  }

  /** Returns the images that this section should render. Will use images set on the section
    * or fetch from the container if applicable.
    */
  def imagesToRender: Iterable[Image] = gallery.images.size match {
    case 0 => Option(item).map(_.images.asScala.map(Image(_, null, 0))).getOrElse(Nil)
    case _ => gallery.images.asScala
  }
}

object VegasSection {
  val RESOURCE_JS = "/com/cloudray/scalapress/plugin/gallery/vegas/vegas.js"
  val js = IOUtils.toString(getClass.getResourceAsStream(RESOURCE_JS))
}