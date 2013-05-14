package com.liferay.scalapress.media

import javax.persistence.{JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import com.liferay.scalapress.plugin.gallery.Gallery
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder
import org.joda.time.{DateTimeZone, DateTime}
import org.hibernate.annotations.{NotFound, NotFoundAction}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "images")
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

    @ManyToOne
    @JoinColumn(name = "item", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var obj: Obj = _

    @ManyToOne
    @JoinColumn(name = "gallery", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var gallery: Gallery = _

    @ManyToOne
    @JoinColumn(name = "category", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    @BeanProperty var folder: Folder = _

    @ManyToOne
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
