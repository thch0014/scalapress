package com.liferay.scalapress.plugin.carousel

import com.liferay.scalapress.section.Section
import javax.persistence.ElementCollection
import scala.beans.BeanProperty
import java.util
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
abstract class CarouselSection extends Section {

    @ElementCollection
    @BeanProperty var images: java.util.Set[String] = new util.HashSet[String]()

    def _images: Iterable[String] = images.size match {
        case 0 => Option(obj).map(_.images.asScala.map(_.filename)).getOrElse(Nil)
        case _ => images.asScala
    }
}
