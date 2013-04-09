package com.liferay.scalapress.plugin.listings

import javax.persistence.{Column, ManyToOne, Entity, Table, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import collection.mutable.ArrayBuffer
import com.liferay.scalapress.obj.ObjectType

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

    @BeanProperty var description: String = _

    @BeanProperty var maxCharacters: Int = _

    @BeanProperty var deleted: Boolean = _

    @BeanProperty var autoPublish: Boolean = _

    @BeanProperty var labels: String = _

    @Column(length = 10000)
    @BeanProperty var name: String = _

    @BeanProperty var fee: Int = _

    @BeanProperty var duration: Int = _

    @BeanProperty var folders: String = _

    def priceText = {
        val buffer = new ArrayBuffer[String]
        buffer.append(fee match {
            case 0 => "Free"
            case _ => "£" + "%.2f".format(fee / 100.0)
        })
        if (duration > 0)
            buffer.append(duration match {
                case x if x == 365 => "for 1 year"
                case x if x == 30 => "for 1 month"
                case x if (x % 30 == 0) => "for " + duration / 30 + " months"
                case x => "for " + x + " days"
            })
        buffer.mkString(" ")
    }

    //
    //    @ManyToMany
    //    @CollectionTable(
    //        name = "listings_packages_categories",
    //        joinColumns = Array(new JoinColumn(name = "listingpackage"))
    //    )
    //    @Column(name = "category")
    //    @BeanProperty var folders: java.util.List[Folder] = new util.ArrayList[Folder]()
}