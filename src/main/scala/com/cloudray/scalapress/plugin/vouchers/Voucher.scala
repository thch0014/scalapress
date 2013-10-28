package com.cloudray.scalapress.plugin.vouchers

import javax.persistence._
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_vouchers")
class Voucher {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @BeanProperty
  @Column(name = "percentDiscount")
  var percentDiscount: Int = _

  @BeanProperty
  @Column(name = "absoluteDiscount")
  var absoluteDiscount: Int = _

  @BeanProperty
  @Column(name = "code")
  var code: String = _

  @BeanProperty
  @Column(name = "start")
  var start: Long = _

  @BeanProperty
  @Column(name = "expiry")
  var expiry: Long = _

  @BeanProperty
  var name: String = _
}
