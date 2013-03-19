package com.liferay.scalapress.domain

import javax.persistence.{JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Table, Entity}
import reflect.BeanProperty
import com.liferay.scalapress.widgets.MediaWidget
import com.liferay.scalapress.plugin.gallery.Gallery
import org.hibernate.annotations.CacheConcurrencyStrategy
import com.liferay.scalapress.obj.Obj

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
    @BeanProperty var obj: Obj = _

    @ManyToOne
    @JoinColumn(name = "gallery", nullable = true)
    @BeanProperty var gallery: Gallery = _

    @ManyToOne
    @JoinColumn(name = "category", nullable = true)
    @BeanProperty var folder: Folder = _

    @ManyToOne
    @JoinColumn(name = "imageBox", nullable = true)
    @BeanProperty var mediaWidget: MediaWidget = _

}

object Image {
    def apply(key: String) = {
        val image = new Image
        image.filename = key
        image.date = System.currentTimeMillis()
        image
    }
}
