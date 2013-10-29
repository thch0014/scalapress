package com.cloudray.scalapress.plugin.vouchers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.form._
import org.joda.time.format.DateTimeFormat
import org.joda.time.DateTimeZone

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/voucher/{id}"))
class VoucherEditController(submissionDao: SubmissionDao,
                            voucherDao: VoucherDao,
                            context: ScalapressContext) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute voucher: Voucher) = "admin/plugin/voucher/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute voucher: Voucher,
           @RequestParam("startString") start: String,
           @RequestParam("expiryString") expiry: String) = {
    voucher.start =
      DateTimeFormat.forPattern("dd-MM-yyyy").parseLocalDate(start).toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis
    voucher.expiry =
      DateTimeFormat.forPattern("dd-MM-yyyy").parseLocalDate(expiry).toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis
    voucherDao.save(voucher)
    edit(voucher)
  }

  @ModelAttribute("voucher")
  def voucher(@PathVariable("id") id: Long) = voucherDao.find(id)
}
