package com.cloudray.scalapress.plugin.listings.controller

import com.cloudray.scalapress.plugin.vouchers.VoucherDao
import org.joda.time.Instant

/** @author Stephen Samuel */
class VoucherService(voucherDao: VoucherDao) {

  def isValidVoucher(code: String) = voucherDao.byCode(code) match {
    case Some(v) => v.start <= Instant.now.getMillis && Instant.now.getMillis <= v.expiry
    case None => false
  }
}
