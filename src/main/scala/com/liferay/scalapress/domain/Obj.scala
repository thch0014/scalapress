package com.liferay.scalapress.domain

import attr.AttributeValue
import reflect.BeanProperty
import java.util
import javax.persistence._

/** @author Stephen Samuel */
@Entity
@Table(name = "items")
class Obj {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var name: String = _

    @BeanProperty var email: String = _

    @BeanProperty var labels: String = _

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "obj", cascade = Array(CascadeType.ALL))
    @BeanProperty var images: java.util.List[Image] = new util.ArrayList[Image]()

    @OneToMany(fetch = FetchType.LAZY,
        mappedBy = "obj",
        cascade = Array(CascadeType.ALL), orphanRemoval = true)
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

    @BeanProperty var confirmationCode: String = _

    @Column(name = "feedSrc", length = 5000)
    @BeanProperty var externalURI: String = _

    @Column(name = "reference", length = 5000)
    @BeanProperty var exernalReference: String = _

    @Column(length = 5000)
    @BeanProperty var descriptionTag: String = _

    @Column(length = 5000)
    @BeanProperty var titleTag: String = _

    @Column(name = "keywords")
    @BeanProperty var keywordsTag: String = _

    @BeanProperty var featured: Boolean = false

    @BeanProperty var summary: String = _

    @Column(name = "genericSellPrice")
    @BeanProperty var sellPrice: Int = _
    def vat = sellPrice * vatRate
    @BeanProperty def sellPriceInc = sellPrice + vat
    @BeanProperty var vatRate: Double = _

    @BeanProperty var costPrice: Int = _

    @Column(name = "rrp")
    @BeanProperty var rrp: Int = _
    def sellPriceDecimal = "%.2f" format sellPrice / 100.0

    @BeanProperty var x: Int = 0
    @BeanProperty var y: Int = 0

    @BeanProperty var location: String = _

    @BeanProperty var status: String = _

    @BeanProperty var inStockMsg: String = _

    @BeanProperty var outStockMsg: String = _

    @Column(name = "ourStock")
    @BeanProperty var stock: Int = _

    def availability = stock match {
        case 0 => outStockMsg
        case _ => inStockMsg
    }

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

    @BeanProperty var ipAddress: String = _

    @BeanProperty var referrer: String = _

    @BeanProperty var permaLink: String = _

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