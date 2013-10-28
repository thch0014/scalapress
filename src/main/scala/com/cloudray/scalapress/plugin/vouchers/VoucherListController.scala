package com.cloudray.scalapress.plugin.vouchers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/voucher"))
class VoucherListController(voucherDao: VoucherDao) {

  @RequestMapping
  def list = "admin/plugin/voucher/list.vm"

  @RequestMapping(value = Array("create"))
  def create: String = {

    val voucher = new Voucher
    voucher.name = "new voucher"
    voucherDao.save(voucher)

    "redirect:/backoffice/voucher/" + voucher.id
  }

  @ModelAttribute("vouchers")
  def forms = voucherDao.findAll().asJava

}
