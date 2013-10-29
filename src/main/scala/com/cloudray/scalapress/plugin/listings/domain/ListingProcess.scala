package com.cloudray.scalapress.plugin.listings.domain

import javax.persistence._
import scala.Array
import org.hibernate.annotations.{BatchSize, FetchMode, Fetch}
import java.util
import com.cloudray.scalapress.item.attr.AttributeValue
import com.cloudray.scalapress.item.Item
import javax.persistence.Column
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_listings_inprocess")
class ListingProcess {

  @Id
  @BeanProperty
  var sessionId: String = _

  @Column(length = 10000)
  @BeanProperty
  var content: String = _

  @BeanProperty
  var email: String = _

  // the last account that was logged in on this listing which might change
  @BeanProperty
  var accountId: String = _

  @ManyToOne
  @BeanProperty
  var listingPackage: ListingPackage = _

  // the completed listing
  @OneToOne(targetEntity = classOf[com.cloudray.scalapress.item.Item])
  @BeanProperty
  var listing: Item = _

  @BeanProperty
  var folders: Array[Long] = Array()

  @BeanProperty
  var title: String = _

  @BeanProperty
  var imageKeys: Array[String] = Array()

  @BeanProperty
  @Column(name = "voucher_code")
  var voucherCode: String = _

  @OneToMany(mappedBy = "listingProcess", fetch = FetchType.LAZY,
    cascade = Array(CascadeType.ALL), orphanRemoval = true)
  @Fetch(FetchMode.SUBSELECT)
  @BatchSize(size = 20)
  @BeanProperty var attributeValues: java.util.Set[AttributeValue] = new util.HashSet[AttributeValue]()
}
