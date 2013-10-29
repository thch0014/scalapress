package com.cloudray.scalapress.plugin.vouchers

import javax.persistence._
import scala.beans.BeanProperty
import org.joda.time.{DateTimeZone, LocalDate}

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
  @Column(name = "fixedDiscount")
  var fixedDiscount: Int = _

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

  @BeanProperty
  var qualifyingAmount: Int = _

  // max number of times a single account can use this voucher, zero for unlimited
  @BeanProperty
  var maxUsesPerAccount: Int = _

  def calculatePrice(base: Int): Int = base - fixedDiscount - (base * percentDiscount / 100d).toInt
}

object Voucher {
  def apply(voucherCodeGenerator: VoucherCodeGenerator) = {
    val voucher = new Voucher
    voucher.name = "new voucher"
    voucher.code = voucherCodeGenerator.generate
    voucher.start = LocalDate.now().toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis
    voucher.expiry = LocalDate.now().plusDays(30).toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis
    voucher
  }
}