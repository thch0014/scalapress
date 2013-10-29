package com.cloudray.scalapress.plugin.listings.controller

import com.cloudray.scalapress.payments.Purchase
import com.cloudray.scalapress.util.Scalate
import org.springframework.validation.Errors

/** @author Stephen Samuel */
class VoucherFormRenderer {

  def render(purchase: Purchase, errors: Errors): String = {
    Scalate.layout(
      "/com/cloudray/scalapress/plugin/listings/voucher.ssp",
      Map("code" -> purchase.voucher.map(_.code),
        "name" -> purchase.voucher.map(_.name),
        "description" -> purchase.voucher.map(_.description))
    )
  }

}
