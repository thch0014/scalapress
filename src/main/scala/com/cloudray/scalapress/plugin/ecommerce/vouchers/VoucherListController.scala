package com.cloudray.scalapress.plugin.ecommerce.vouchers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/voucher"))
class VoucherListController(voucherDao: VoucherDao,
                            voucherCodeGenerator: VoucherCodeGenerator) {

  @RequestMapping
  def list = "admin/plugin/ecommerce/voucher/list.vm"

  @RequestMapping(value = Array("create"))
  def create: String = {
    val voucher = Voucher(voucherCodeGenerator)
    voucherDao.save(voucher)
    "redirect:/backoffice/plugin/voucher/" + voucher.id
  }

  @ModelAttribute("vouchers")
  def forms = voucherDao.findAll.asJava

}
