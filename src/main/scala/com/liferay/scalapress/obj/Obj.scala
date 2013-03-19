package com.liferay.scalapress.obj

import attr.AttributeValue
import reflect.BeanProperty
import java.util
import javax.persistence._
import org.hibernate.annotations.{Index, BatchSize, FetchMode, Fetch}
import com.liferay.scalapress.section.Section
import com.liferay.scalapress.plugin.listings.ListingPackage
import com.liferay.scalapress.folder.Folder
import com.liferay.scalapress.media.Image

/** @author Stephen Samuel */
@Entity
@Table(name = "items")
class Obj {

    def available = stock > 0

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @Index(name = "name_index")
    @BeanProperty var name: String = _

    @BeanProperty var expiry: Long = 0

    @BeanProperty var email: String = _

    @BeanProperty var labels: String = _

    @Index(name = "owner_index")
    @ManyToOne
    @JoinColumn(name = "account")
    @BeanProperty var account: Obj = _

    @ManyToOne
    @JoinColumn(name = "listing_package")
    @BeanProperty var listingPackage: ListingPackage = _

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "obj", cascade = Array(CascadeType.ALL))
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 20)
    @BeanProperty var images: java.util.Set[Image] = new util.HashSet[Image]()

    @OneToMany(mappedBy = "obj", fetch = FetchType.LAZY,
        cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 20)
    @BeanProperty var attributeValues: java.util.Set[AttributeValue] = new util.HashSet[AttributeValue]()

    @ManyToMany(fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
    @JoinTable(name = "categories_items",
        joinColumns = Array(new JoinColumn(name = "item", unique = true)),
        inverseJoinColumns = Array(new JoinColumn(name = "category"))
    )
    @BeanProperty var folders: java.util.Set[Folder] = new util.HashSet[Folder]()

    @ManyToMany(fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
    @JoinTable(name = "items_accessories",
        joinColumns = Array(new JoinColumn(name = "item", unique = true)),
        inverseJoinColumns = Array(new JoinColumn(name = "accessory"))
    )
    @BeanProperty var associations: java.util.Set[Obj] = new util.HashSet[Obj]()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "obj", cascade = Array(CascadeType.ALL))
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 20)
    @BeanProperty var sections: java.util.Set[Section] = new util.HashSet[Section]()

    @Index(name = "objecttype_index")
    @ManyToOne
    @JoinColumn(name = "itemType")
    @BeanProperty var objectType: ObjectType = _

    @Column
    @BeanProperty var content: String = _
    def content(limit: Int): String = content.take(limit)

    @BeanProperty var dateCreated: java.lang.Long = _
    @BeanProperty var dateUpdated: java.lang.Long = _

    @Column(name = "reference", length = 5000)
    @BeanProperty var exernalReference: String = _

    @Column(length = 5000)
    @BeanProperty var descriptionTag: String = _

    @Column(length = 5000)
    @BeanProperty var titleTag: String = _

    @Column(name = "keywords", length = 5000)
    @BeanProperty var keywordsTag: String = _

    @Column(length = 5000)
    @BeanProperty var summary: String = _

    @Index(name = "sellPrice_index")
    @Column(name = "genericSellPrice")
    @BeanProperty var sellPrice: Int = _

    @BeanProperty def vat: Int = (sellPrice * vatRate / 100.0).toInt
    @BeanProperty def sellPriceInc: Int = sellPrice + vat
    @BeanProperty def sellPriceDecimal = "%.2f" format sellPrice / 100.0

    @BeanProperty var vatRate: Double = _

    @BeanProperty var costPrice: Int = _

    @Column(name = "rrp")
    @BeanProperty var rrp: Int = _

    @BeanProperty var x: Int = 0
    @BeanProperty var y: Int = 0

    @BeanProperty var location: String = _

    @Index(name = "status_index")
    @BeanProperty var status: String = _

    @BeanProperty var inStockMsg: String = _

    @BeanProperty var outStockMsg: String = _

    @Column(name = "ourStock")
    @BeanProperty var stock: Int = _

    @Column(name = "brochure")
    @BeanProperty var orderable: Boolean = false

    @BeanProperty var orderQtyMin: Int = _
    @BeanProperty var orderQtyMax: Int = _
    @BeanProperty var backorders: Boolean = _

    // used by account types
    @BeanProperty var passwordHash: String = _

    @BeanProperty var ipAddress: String = _

    @BeanProperty var permaLink: String = _

    @PrePersist
    def updateLastModified() {
        dateUpdated = System.currentTimeMillis()
    }

}

object Obj {
    def apply(t: ObjectType) = {
        require(t != null)

        val obj = new Obj
        obj.objectType = t
        obj.name = "new object"
        obj.dateCreated = System.currentTimeMillis
        obj.dateUpdated = System.currentTimeMillis
        obj.status = "Live"
        obj
    }
}