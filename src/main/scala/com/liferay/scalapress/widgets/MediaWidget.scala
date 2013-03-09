package com.liferay.scalapress.widgets

import javax.persistence.{CascadeType, FetchType, OneToMany, Table, Entity}
import reflect.BeanProperty
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import scala.Array
import java.util
import com.liferay.scalapress.domain.Image
import scala.collection.JavaConverters._
import com.liferay.scalapress.service.ImageRenderer

/** @author Stephen Samuel */

@Entity
@Table(name = "boxes_images")
class MediaWidget extends Widget {

    @BeanProperty var url: String = _

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mediaWidget", cascade = Array(CascadeType.ALL))
    @BeanProperty var images: java.util.List[Image] = new util.ArrayList[Image]()

    override def render(req: ScalapressRequest, context: ScalapressContext): Option[String] = {
        images.asScala.headOption match {
            case None => None
            case Some(image) => {
                val html = Option(url) match {
                    case None => ImageRenderer.link(image)
                    case Some(u) => "<a href='" + u + "'>" + ImageRenderer.link(image) + "</a>"
                }
                Some(html)
            }
        }
    }
}
