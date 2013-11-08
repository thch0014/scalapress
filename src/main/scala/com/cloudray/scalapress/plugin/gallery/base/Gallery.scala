package com.cloudray.scalapress.plugin.gallery.base

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
  @BeanProperty
  var id: Long = _

  @BeanProperty
  var name: String = _

  @Column(length = 2000)
  @BeanProperty
  var description: String = _

  @BeanProperty
  @deprecated
  var params: String = _

  @CollectionTable(name = "Gallery_images")
  @ElementCollection(fetch = FetchType.EAGER)
  @BeanProperty var images: java.util.List[Image] = new util.ArrayList[Image]()
  def sortedImages: Seq[Image] = images.asScala.toSeq
}
