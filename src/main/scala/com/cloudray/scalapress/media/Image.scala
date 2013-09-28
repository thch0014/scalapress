package com.cloudray.scalapress.media

import javax.persistence._
import scala.beans.BeanProperty

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

  @BeanProperty var item: String = _
  @BeanProperty var gallery: String = _
  @BeanProperty var imageBox: String = _
}
