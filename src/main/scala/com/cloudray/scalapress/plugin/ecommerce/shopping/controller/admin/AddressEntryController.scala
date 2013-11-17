package com.cloudray.scalapress.plugin.ecommerce.shopping.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, ModelAttribute, RequestMapping}
import scala.Array
import javax.validation.Valid
import org.springframework.validation.Errors
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.item.ItemDao
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.AddressDao
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.Address

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/shopping/address"))
class AddressEntryController(context: ScalapressContext,
                             addressDao: AddressDao,
                             objectDao: ItemDao) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit = "admin/plugin/ecommerce/shopping/address/entry.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def submit(@RequestParam("accountId") accountId: Long,
             @RequestParam("orderId") orderId: Long,
             @Valid @ModelAttribute("address") address: Address,
             errors: Errors) = {

    if (errors.hasErrors)
      edit
    else {
      address.id = 0
      address.active = true
      address.account = accountId.toString
      addressDao.save(address)
      "redirect:/backoffice/order/" + orderId
    }
  }

  @ModelAttribute("orderId")
  def orderId(@RequestParam("orderId") orderId: Long) = orderId

  @ModelAttribute("address")
  def address = new Address()
}
