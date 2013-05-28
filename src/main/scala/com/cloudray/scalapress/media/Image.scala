package com.cloudray.scalapress.media

import javax.persistence._
import com.cloudray.scalapress.plugin.gallery.Gallery
import com.cloudray.scalapress.obj.Obj
import com.cloudray.scalapress.folder.Folder
import org.joda.time.{DateTimeZone, DateTime}
import org.hibernate.annotations.{NotFound, NotFoundAction}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "images")
@deprecated("Images should really just be stored as strings now")
class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var filename: String = _
    @BeanProperty var description: String = _
    @BeanProperty var name: String = _

    @BeanProperty var date: Long = _
    @BeanProperty var position: Int = _
    @BeanProperty var alt: String = _

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
    @JoinColumn(name = "category", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var folder: Folder = _

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageBox", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var mediaWidget: MediaWidget = _

}

object Image {
    def apply(key: String) = {
        val image = new Image
        image.filename = key
        image.date = new DateTime(DateTimeZone.UTC).getMillis
        image
    }
}
