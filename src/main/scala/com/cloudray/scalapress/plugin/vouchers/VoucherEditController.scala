package com.cloudray.scalapress.plugin.vouchers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.form._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/voucher/{id}"))
class VoucherEditController(submissionDao: SubmissionDao,
                            voucherDao: VoucherDao,
                            context: ScalapressContext) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute voucher: Voucher) = "admin/plugin/voucher/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute voucher: Voucher) = {
    voucherDao.save(voucher)
    edit(voucher)
  }

  @ModelAttribute("voucher")
  def voucher(@PathVariable("id") id: Long) = voucherDao.find(id)
}
