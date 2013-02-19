package com.liferay.scalapress.plugin.listings

import javax.persistence.{Column, ManyToOne, Entity, Table, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.domain.ObjectType

/** @author Stephen Samuel */
@Entity
@Table(name = "listings_packages")
class ListingPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @Column(name = "maxcategories")
    @BeanProperty var maxFolders: Int = _

    @ManyToOne
    @BeanProperty var objectType: ObjectType = _

    @BeanProperty var maxImages: Int = _

    @BeanProperty var maxCharacters: Int = _

    @BeanProperty var labels: String = _

    @BeanProperty var name: String = _

    @BeanProperty var fee: Int = _
}