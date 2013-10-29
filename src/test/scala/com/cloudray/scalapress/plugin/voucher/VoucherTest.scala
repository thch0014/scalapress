package com.cloudray.scalapress.plugin.voucher

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.vouchers.Voucher

/** @author Stephen Samuel */
class VoucherTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val voucher = new Voucher

  test("voucher should apply fixed discount") {
    voucher.fixedDiscount = 1234
    val actual = voucher.calculatePrice(9876)
    assert(9876 - 1234 === actual)
  }

  test("voucher should apply percentage discount") {
    voucher.fixedDiscount = 0
    voucher.percentDiscount = 15
    val actual = voucher.calculatePrice(1000)
    assert(850 === actual)
  }
}
