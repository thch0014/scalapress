package com.cloudray.scalapress.plugin.carousel

import com.cloudray.scalapress.section.Section
import javax.persistence.ElementCollection
import scala.beans.BeanProperty
import java.util
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
abstract class CarouselSection extends Section {

  @ElementCollection
  @BeanProperty var images: java.util.Set[String] = new util.HashSet[String]()

  def _images: Iterable[String] = images.asScala
}
