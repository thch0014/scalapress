package com.liferay.scalapress.plugin.gallery

import scala.Predef.String
import reflect.BeanProperty
import javax.persistence.{CascadeType, FetchType, OneToMany, Table, Entity, GenerationType, GeneratedValue, Id}
import scala.Array
import java.util
import com.liferay.scalapress.media.Image

/** @author Stephen Samuel */
@Entity
@Table(name = "galleries")
class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var name: String = _
    @BeanProperty var description: String = null

    @BeanProperty var showDateUploaded: Boolean = _

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "gallery", cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @BeanProperty var images: java.util.List[Image] = new util.ArrayList[Image]()

}
