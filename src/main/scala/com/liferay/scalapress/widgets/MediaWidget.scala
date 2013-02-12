package com.liferay.scalapress.widgets

import javax.persistence.{CascadeType, FetchType, OneToMany, Table, Entity}
import reflect.BeanProperty
import com.liferay.scalapress.ScalapressRequest
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mediaWidget", cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @BeanProperty var images: java.util.List[Image] = new util.ArrayList[Image]()

    override def backoffice = "/backoffice/widget/media/" + id

    override def render(req: ScalapressRequest): Option[String] = {
        images.asScala.headOption match {
            case None => None
            case Some(image) => {
                val src = req.context.assetStore.link(image.filename)
                val html = Option(url) match {
                    case None => ImageRenderer.link(src)
                    case Some(u) => "<a href='" + u + "'>" + ImageRenderer.link(src) + "</a>"
                }
                Some(html)
            }
        }
    }
}
