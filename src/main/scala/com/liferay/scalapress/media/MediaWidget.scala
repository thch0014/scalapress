package com.liferay.scalapress.media

import javax.persistence.{CascadeType, FetchType, OneToMany, Table, Entity}
import com.liferay.scalapress.ScalapressRequest
import scala.Array
import java.util
import scala.collection.JavaConverters._
import org.hibernate.annotations.{NotFound, NotFoundAction, FetchMode, Fetch}
import com.liferay.scalapress.widgets.Widget
import scala.beans.BeanProperty

/** @author Stephen Samuel */

@Entity
@Table(name = "boxes_images")
class MediaWidget extends Widget {

    @BeanProperty var url: String = _

    @OneToMany(fetch = FetchType.EAGER,
        mappedBy = "mediaWidget",
        cascade = Array(CascadeType.ALL),
        orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var images: java.util.Set[Image] = new util.HashSet[Image]()

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
