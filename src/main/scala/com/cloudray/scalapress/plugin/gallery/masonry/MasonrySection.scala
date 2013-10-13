package com.cloudray.scalapress.plugin.gallery.masonry

import com.cloudray.scalapress.ScalapressRequest
import javax.persistence.{ElementCollection, Entity, Table}
import com.cloudray.scalapress.section.Section
import java.util
import scala.collection.JavaConverters._
import com.cloudray.scalapress.plugin.gallery.GalleryImage

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_gallery_masonry")
class MasonrySection extends Section {

  @ElementCollection
  var images: java.util.Set[GalleryImage] = new util.HashSet[GalleryImage]()
  var script: String = _

  override def desc: String = "A section showing a gallery using masonry"
  override def backoffice: String = "/backoffice/plugin/gallery/masonry/section/" + id

  override def render(request: ScalapressRequest): Option[String] = {
    val rows = imagesToRender.map(imageHtml).mkString("\n")
    val container = <div class="masonry_container"></div>
    val script = ""
    Some(rows + container + script)
  }

  def imageHtml(image: GalleryImage) = {
    val fullsize = "/images/" + image.key + "?width=800&height=600"
    val thumbnail = "/images/" + image.key + "?width=100&height=100"
    <li>
      <a class="masonry-thumb" href={fullsize}>
        <img src={thumbnail}/>
      </a>
    </li>
  }

  /** Returns the images that this section should render. Will use images set on the section
    * or fetch from the container if applicable.
    */
  def imagesToRender: Iterable[GalleryImage] = images.size match {
    case 0 => Option(obj).flatMap(_.images.asScala).map(GalleryImage(_, null))
    case _ => images.asScala
  }

}