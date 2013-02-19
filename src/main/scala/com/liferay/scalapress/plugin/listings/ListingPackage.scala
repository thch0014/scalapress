package com.liferay.scalapress.plugin.listings

import javax.persistence.{ManyToMany, JoinColumn, CollectionTable, Column, ManyToOne, Entity, Table, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.domain.{Folder, ObjectType}
import java.util

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

    @Column(length = 10000)
    @BeanProperty var name: String = _

    @BeanProperty var fee: Int = _

    @BeanProperty var duration: Int = _

    @ManyToMany
    @CollectionTable(
        name = "listings_packages_categories",
        joinColumns = Array(new JoinColumn(name = "listingpackage"))
    )
    @Column(name = "category")
    @BeanProperty var folders: java.util.List[Folder] = new util.ArrayList[Folder]()
}