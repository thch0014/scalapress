package com.cloudray.scalapress.item

import attr.AttributeValue
import java.util
import javax.persistence._
import com.cloudray.scalapress.section.{SortedSections, Section}
import com.cloudray.scalapress.folder.{HtmlMeta, Folder}
import org.joda.time.{DateTimeZone, DateTime}
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.CascadeType
import scala.collection.JavaConverters._
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.listings.domain.ListingPackage
import org.hibernate.annotations._
import com.cloudray.scalapress.account.Account
import javax.persistence.Column

/** @author Stephen Samuel */
@Entity
@Table(name = "items")
class Item extends SortedSections with java.io.Serializable with HtmlMeta {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Index(name = "id_index")
  @BeanProperty
  var id: Long = _

  @Index(name = "name_index")
  @BeanProperty
  var name: String = _

  //  @deprecated
  //  @Column(name = "email")
  //  @BeanProperty
  //  var email_deprecated: String = _
  //
  //  @deprecated
  //  @BeanProperty
  //  @Column(name = "passwordHash")
  //  var password_deprecated: String = _

  @BeanProperty
  var expiry: Long = 0

  @BeanProperty
  var labels: String = _

  @BeanProperty
  var prioritized: Boolean = _

  @Index(name = "owner_index")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var account: Account = _

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "listing_package")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var listingPackage: ListingPackage = _

  @ElementCollection(fetch = FetchType.EAGER)
  @Fetch(FetchMode.SUBSELECT)
  @CollectionTable(name = "Obj_images",
    joinColumns = Array(new JoinColumn(name = "Obj_id"))
  )
  @BeanProperty
  var images: java.util.List[String] = new util.ArrayList[String]()
  def sortedImages = images.asScala.toSeq

  @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL), orphanRemoval = true)
  @Fetch(FetchMode.SELECT)
  @BatchSize(size = 20)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var attributeValues: java.util.Set[AttributeValue] = new util.HashSet[AttributeValue]()
  def sortedAttributeValues: Seq[AttributeValue] = attributeValues
    .asScala
    .filterNot(_.value == null)
    .filterNot(_.value.isEmpty)
    .toSeq
    .sortBy(_.value)
    .sortBy(_.attribute.name)
    .sortBy(_.attribute.position)

  @ManyToMany(fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
  @JoinTable(name = "categories_items",
    joinColumns = Array(new JoinColumn(name = "item", unique = true)),
    inverseJoinColumns = Array(new JoinColumn(name = "category"))
  )
  @NotFound(action = NotFoundAction.IGNORE)
  @Fetch(FetchMode.SELECT)
  @BatchSize(size = 5)
  @BeanProperty
  var folders: java.util.Set[Folder] = new util.HashSet[Folder]()

  @ManyToMany(fetch = FetchType.LAZY, cascade = Array(CascadeType.ALL))
  @JoinTable(name = "items_accessories",
    joinColumns = Array(new JoinColumn(name = "item", unique = true)),
    inverseJoinColumns = Array(new JoinColumn(name = "accessory"))
  )
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var associations: java.util.Set[Item] = new util.HashSet[Item]()

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = Array(CascadeType.ALL))
  @Fetch(FetchMode.SELECT)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var sections: java.util.Set[Section] = new util.HashSet[Section]()

  @Index(name = "itemtype_index")
  @ManyToOne
  @JoinColumn(name = "itemType")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var itemType: ItemType = _

  @deprecated
  def objectType = itemType
  @deprecated
  def getObjectType = itemType

  @Column(length = 10000)
  @BeanProperty var content: String = _
  def content(limit: Int): String = content.take(limit)

  @Column(name = "dateCreated")
  @BeanProperty
  var dateCreated: java.lang.Long = _

  @Column(name = "dateUpdated")
  @BeanProperty
  var dateUpdated: java.lang.Long = _

  @Column(name = "reference", length = 5000)
  @BeanProperty
  var exernalReference: String = _

  @Column(length = 5000)
  @BeanProperty
  var summary: String = _

  def summary(max: Int): Option[String] = {
    Option(content)
      .map(_.replaceAll("<.*?>", ""))
      .map(_.take(max).reverse.dropWhile(_ != ' ').reverse.trim + "...")
  }

  @Index(name = "sellPrice_index")
  @Column(name = "genericSellPrice")
  @BeanProperty
  var price: Int = _

  @BeanProperty def vat: Int = (price * vatRate / 100.0).toInt
  @BeanProperty def sellPriceInc: Int = price + vat
  @BeanProperty def sellPriceDecimal = "%.2f" format price / 100.0
  def profit = price - costPrice

  @Column(name = "vatRate")
  @BeanProperty
  var vatRate: Double = _

  @Column(name = "costPrice")
  @BeanProperty
  var costPrice: Int = _

  @Column(name = "rrp")
  @BeanProperty
  var rrp: Int = _

  @Index(name = "status_index")
  @BeanProperty
  var status: String = _

  @Column(name = "inStockMsg")
  @BeanProperty
  var inStockMsg: String = _

  @Column(name = "outStockMsg")
  @BeanProperty
  var outStockMsg: String = _

  @Column(name = "ourStock")
  @BeanProperty
  var stock: Int = _
  def available = stock > 0

  @Column(name = "brochure", nullable = false)
  @BeanProperty
  var orderable: Boolean = false

  @Column(name = "backorders", nullable = false)
  @BeanProperty
  var backorders: Boolean = _

  @PrePersist
  def updateLastModified(): Unit = dateUpdated = new DateTime(DateTimeZone.UTC).getMillis

  override def toString: String = s"Item [id=$id, name=$name, status=$status]"

  def isDeleted = Item.STATUS_DELETED.equalsIgnoreCase(status)
  def isDisabled = Item.STATUS_DISABLED.equalsIgnoreCase(status)
  def isLive = Item.STATUS_LIVE.equalsIgnoreCase(status)
}

object Item {
  val STATUS_DELETED_LOWER = "deleted"
  val STATUS_LIVE_LOWER = "live"
  val STATUS_DISABLED_LOWER = "disabled"
  val STATUS_DELETED = "Deleted"
  val STATUS_LIVE = "Live"
  val STATUS_DISABLED = "Disabled"

  def apply(t: ItemType) = {
    require(t != null)

    val obj = new Item
    obj.itemType = t
    obj.name = "new item"
    obj.dateCreated = new DateTime(DateTimeZone.UTC).getMillis
    obj.dateUpdated = obj.dateCreated
    obj.status = STATUS_LIVE
    obj
  }
}