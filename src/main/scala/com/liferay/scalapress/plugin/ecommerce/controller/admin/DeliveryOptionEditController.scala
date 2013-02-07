package com.liferay.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.ecommerce.dao.DeliveryOptionDao
import com.liferay.scalapress.plugin.ecommerce.domain.DeliveryOption

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/delivery/{id}"))
class DeliveryOptionEditController {

    @Autowired var deliveryOptionDao: DeliveryOptionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute("option") deliveryOption: DeliveryOption) = "admin/delivery/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute("option") deliveryOption: DeliveryOption) = {
        deliveryOptionDao.save(deliveryOption)
        edit(deliveryOption)
    }

    @ModelAttribute("option") def del(@PathVariable("id") id: Long) = deliveryOptionDao.find(id)
}
