package com.cloudray.scalapress.plugin.ecommerce.vouchers

import org.joda.time.Instant

/** @author Stephen Samuel */
class VoucherService(voucherDao: VoucherDao) {

  def isValidVoucher(code: String): Boolean = voucherDao.byCode(code).exists(isValidVoucher)
  def isValidVoucher(v: Voucher): Boolean = v.start <= Instant.now.getMillis && Instant.now.getMillis <= v.expiry
}
