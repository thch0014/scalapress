package com.cloudray.scalapress.plugin.gallery.galleryview

import javax.persistence._
import java.util
import com.cloudray.scalapress.media.Image
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFound, NotFoundAction}

/** @author Stephen Samuel */
@Entity
@Table(name = "galleries")
class Gallery {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  @BeanProperty var name: String = _

  @Column(length = 2000)
  @BeanProperty var description: String = null

  @BeanProperty var params: String = null

  @BeanProperty var showDateUploaded: Boolean = _

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "gallery", cascade = Array(CascadeType.ALL), orphanRemoval = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var images: java.util.List[Image] = new util.ArrayList[Image]()

}
