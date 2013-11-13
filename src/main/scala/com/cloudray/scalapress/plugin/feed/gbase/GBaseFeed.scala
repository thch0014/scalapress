package com.cloudray.scalapress.plugin.feed.gbase

import javax.persistence._
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "feeds_googlebase")
class GBaseFeed {

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  @BeanProperty
  var id: Long = _

  @Column(name = "disabled", nullable = false)
  @BeanProperty
  var disabled = false
  def enabled = !disabled

  @BeanProperty
  var lastRuntime: Long = _

  @BeanProperty
  var ftpHostname: String = _

  @BeanProperty
  var ftpUsername: String = _

  @BeanProperty
  var ftpPassword: String = _

  @BeanProperty
  var ftpFilename: String = _

  @BeanProperty
  var brandAttrName: String = _

  @BeanProperty
  var partAttrName: String = _

  @BeanProperty
  var shippingCost: String = _

  @Column(name = "productType")
  @BeanProperty
  var productCategory: String = _
}
