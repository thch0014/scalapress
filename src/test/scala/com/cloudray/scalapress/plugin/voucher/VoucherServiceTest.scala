package com.cloudray.scalapress.plugin.voucher

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.vouchers.{Voucher, VoucherDao, VoucherService}
import scala.concurrent.duration._

/** @author Stephen Samuel */
class VoucherServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val dao = mock[VoucherDao]
  val service = new VoucherService(dao)

  val voucher = new Voucher
  voucher.start = System.currentTimeMillis() - (1 days).toMillis
  voucher.expiry = System.currentTimeMillis() + (1 days).toMillis

  test("voucher should be valid if within date range") {
    assert(service.isValidVoucher(voucher))
  }

  test("voucher should be invalid if expired") {
    voucher.expiry = System.currentTimeMillis() - (1 days).toMillis
    assert(!service.isValidVoucher(voucher))
  }

  test("voucher should be invalid if not yet started") {
    voucher.start = System.currentTimeMillis() + (1 days).toMillis
    assert(!service.isValidVoucher(voucher))
  }
}
