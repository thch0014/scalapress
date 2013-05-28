package com.cloudray.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, PathVariable, RequestMethod, ModelAttribute, RequestMapping}
import com.cloudray.scalapress.plugin.ecommerce.domain.Address
import scala.Array
import javax.validation.Valid
import org.springframework.validation.Errors
import com.cloudray.scalapress.ScalapressContext
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.ecommerce.dao.AddressDao
import com.cloudray.scalapress.obj.ObjectDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/shopping/address/{id}"))
class AddressEntryController {

    @Autowired var context: ScalapressContext = _
    @Autowired var addressDao: AddressDao = _
    @Autowired var objectDao: ObjectDao = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit = "admin/plugin/shopping/address/entry.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def submit(@PathVariable("id") accountId: Long,
               @RequestParam("orderId") orderId: Long,
               @Valid @ModelAttribute("address") address: Address,
               errors: Errors) = {

        if (errors.hasErrors)
            edit
        else {
            address.active = true
            address.account = accountId.toString
            addressDao.save(address)
            "redirect:/backoffice/order/" + orderId
        }
    }

    @ModelAttribute("orderId") def orderId(@RequestParam("orderId") orderId: Long) = orderId
    @ModelAttribute("address") def address = new Address()
}
