package com.liferay.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.ecommerce.dao.DeliveryOptionDao
import com.liferay.scalapress.plugin.ecommerce.domain.DeliveryOption
import reflect.BeanProperty

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/delivery/{id}"))
class DeliveryOptionEditController {

    @Autowired var deliveryOptionDao: DeliveryOptionDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("form") form: Form) = "admin/delivery/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("form") form: Form) = {
        form.option.charge = (form.charge * 100).toInt
        form.option.minPrice = (form.minPrice * 100).toInt
        form.option.maxPrice = (form.maxPrice * 100).toInt
        deliveryOptionDao.save(form.option)
        "redirect:/backoffice/delivery/" + form.option.id
    }

    @ModelAttribute("form") def del(@PathVariable("id") id: Long) = {
        val form = new Form
        form.option = deliveryOptionDao.find(id)
        form.charge = form.option.charge / 100.0
        form.minPrice = form.option.minPrice / 100.0
        form.maxPrice = form.option.maxPrice / 100.0
        form
    }
}

class Form {
    @BeanProperty var option: DeliveryOption = _
    @BeanProperty var charge: Double = _
    @BeanProperty var minPrice: Double = _
    @BeanProperty var maxPrice: Double = _
}
