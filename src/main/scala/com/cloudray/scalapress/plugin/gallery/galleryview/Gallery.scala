package com.cloudray.scalapress.plugin.gallery.galleryview

import javax.persistence._
import java.util
import scala.beans.BeanProperty
import scala.collection.JavaConverters._

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

  @ElementCollection(fetch = FetchType.EAGER)
  @BeanProperty var images: java.util.List[String] = new util.ArrayList[String]()
  def sortedImages = images.asScala.toSeq
}
