package com.cloudray.scalapress.plugin.gallery.masonry

import javax.persistence._
import com.cloudray.scalapress.section.Section
import scala.collection.JavaConverters._
import com.cloudray.scalapress.util.Scalate
import com.cloudray.scalapress.media.AssetStore
import org.apache.commons.io.IOUtils
import com.cloudray.scalapress.framework.ScalapressRequest
import com.cloudray.scalapress.plugin.gallery.base.{Gallery, Image}
import scala.Some
import org.hibernate.annotations.{NotFoundAction, NotFound}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_gallery_masonry")
class MasonrySection extends Section {

  private val SSP_RESOURCE = "/com/cloudray/scalapress/plugin/gallery/masonry/masonry_image.ssp"
  private val CONTAINER_START = "<div class=\"masonry-container\">"
  private val CONTAINER_END = "</div>"

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gallery")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var gallery: Gallery = _

  override def desc: String = "Showing a gallery using Masonry plugin"
  override def backoffice: String = "/backoffice/plugin/gallery/masonry/section/" + id

  override def render(request: ScalapressRequest): Option[String] = {
    val assetStore = request.context.bean[AssetStore]
    val rows = imagesToRender.map(imageHtml(assetStore, _)).mkString("\n")
    val js = IOUtils.toString(getClass.getResourceAsStream(MasonrySection.JS_RESOURCE))
    val css = IOUtils.toString(getClass.getResourceAsStream(MasonrySection.CSS_RESOURCE))

    val components = List("<script>", js, "</script><style>", css, "</style>", CONTAINER_START, rows, CONTAINER_END)
    Some(components.mkString("\n"))
  }

  /** Renders the HTML for a single image
    */
  def imageHtml(assetStore: AssetStore, image: Image): String = {
    val url = assetStore.link(image.key)
    Scalate.layout(SSP_RESOURCE, Map("caption" -> Option(image.description), "imageUrl" -> url))
  }

  /** Returns the images that this section should render. Will use images set on the section
    * or fetch from the container if applicable.
    */
  def imagesToRender: Iterable[Image] = gallery.images.size match {
    case 0 => Option(item).map(_.images.asScala.map(Image(_, null, 0))).getOrElse(Nil)
    case _ => gallery.images.asScala
  }
}

object MasonrySection {
  private val JS_RESOURCE = "/com/cloudray/scalapress/plugin/gallery/masonry/masonry.js"
  private val CSS_RESOURCE = "/com/cloudray/scalapress/plugin/gallery/masonry/masonry.css"
  val js = IOUtils.toString(getClass.getResourceAsStream(JS_RESOURCE))
  val css = IOUtils.toString(getClass.getResourceAsStream(CSS_RESOURCE))
}