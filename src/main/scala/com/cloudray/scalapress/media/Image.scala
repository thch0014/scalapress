package com.cloudray.scalapress.media

import javax.persistence._
import com.cloudray.scalapress.obj.Obj
import org.hibernate.annotations.{NotFound, NotFoundAction}
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.gallery.galleryview.Gallery

/** @author Stephen Samuel */
@Entity
@Table(name = "images")
@deprecated("Images should really just be stored as strings now")
class Image extends java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  @BeanProperty var filename: String = _

  @BeanProperty var position: Int = _

  //@BeanProperty var contentType: String = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var obj: Obj = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gallery", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var gallery: Gallery = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "imageBox", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var mediaWidget: MediaWidget = _

}
