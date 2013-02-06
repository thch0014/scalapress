package com.liferay.scalapress.domain

import attr.AttributeValue
import javax.persistence._
import reflect.BeanProperty
import java.util

/** @author Stephen Samuel */
@Entity
@Table(name = "items")
class Obj {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var name: String = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var email: String = _

    @ElementCollection
    @CollectionTable(
        name = "object_labels",
        joinColumns = Array(new JoinColumn(name = "object_id"))
    )
    @BeanProperty var labels: java.util.Set[String] = new util.HashSet[String]()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "obj", cascade = Array(CascadeType.ALL))
    @BeanProperty var images: java.util.List[Image] = new util.ArrayList[Image]()

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "obj", cascade = Array(CascadeType.ALL))
    @BeanProperty var attributeValues: java.util.List[AttributeValue] = new util.ArrayList[AttributeValue]()

    @ManyToMany(fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
    @JoinTable(name = "categories_items",
        joinColumns = Array(new JoinColumn(name = "item", unique = true)),
        inverseJoinColumns = Array(new JoinColumn(name = "category"))
    )
    @BeanProperty var folders: java.util.List[Folder] = new util.ArrayList[Folder]()

    @ManyToOne
    @JoinColumn(name = "itemType")
    @BeanProperty var objectType: ObjectType = _

    @Column
    @BeanProperty var content: String = _
    def content(limit: Int): String = content.take(limit)

    @BeanProperty var dateCreated: java.lang.Long = _
    @BeanProperty var dateUpdated: java.lang.Long = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var confirmationCode: String = _

    @Column(name = "feedSrc", columnDefinition = "TEXT")
    @BeanProperty var externalURI: String = _

    @Column(name = "reference", columnDefinition = "TEXT")
    @BeanProperty var exernalReference: String = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var descriptionTag: String = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var titleTag: String = _

    @Column(name = "keywords")
    @BeanProperty var keywordsTag: String = _

    @BeanProperty var featured: Boolean = false

    @Column(columnDefinition = "TEXT")
    @BeanProperty var summary: String = _

    @Column(name = "genericSellPrice")
    @BeanProperty var sellPrice: Int = _
    @BeanProperty def sellPriceInc = sellPrice * vatRate
    @BeanProperty var costPrice: Int = _
    @BeanProperty var vatRate: Double = _

    @Column(name = "rrp")
    @BeanProperty var rrp: Int = _
    def sellPriceDecimal = "%.2f" format sellPrice / 100.0

    @BeanProperty var x: Int = 0
    @BeanProperty var y: Int = 0
    @Column(columnDefinition = "TEXT")
    @BeanProperty var location: String = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var status: String = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var inStockMsg: String = _
    @Column(columnDefinition = "TEXT")
    @BeanProperty var outStockMsg: String = _

    @Column(name = "ourStock")
    @BeanProperty var stock: Int = _

    @Column(name = "brochure")
    @BeanProperty var orderable: Boolean = false

    @BeanProperty var orderQtyMin: Int = _
    @BeanProperty var orderQtyMax: Int = _
    @BeanProperty var orderQtyMultiple: Int = _
    @BeanProperty var backorders: Boolean = _
    @BeanProperty var stockNotifyLevel: Int = _

    // used by account types
    @BeanProperty var passwordHash: String = _
    @BeanProperty var resetCode: String = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var ipAddress: String = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var referrer: String = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var mobilePhone: String = _

    @Column(columnDefinition = "TEXT")
    @BeanProperty var permaLink: String = _

}

object Obj {
    def apply(t: ObjectType) = {
        val obj = new Obj
        obj.objectType = t
        obj.name = "new object"
        obj.dateCreated = System.currentTimeMillis
        obj.dateUpdated = System.currentTimeMillis
        obj.status = "Live"
        obj
    }
}