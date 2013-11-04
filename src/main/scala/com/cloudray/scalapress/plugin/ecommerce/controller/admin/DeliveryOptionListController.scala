package com.cloudray.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestBody, RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.cloudray.scalapress.plugin.ecommerce.dao.DeliveryOptionDao
import com.cloudray.scalapress.plugin.ecommerce.domain.DeliveryOption
import scala.collection.JavaConverters._
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/delivery"))
class DeliveryOptionListController(deliveryOptionDao: DeliveryOptionDao,
                                   context: ScalapressContext) {

  @RequestMapping
  def list = "admin/plugin/shopping/delivery/list.vm"

  @RequestMapping(value = Array("create"))
  def create = {
    val d = new DeliveryOption
    deliveryOptionDao.save(d)
    "redirect:/backoffice/delivery"
  }

  @ResponseBody
  @RequestMapping(value = Array("/order"), method = Array(RequestMethod.POST))
  def reorder(@RequestBody order: String): String = {

    val ids = order.split("-")
    deliveryOptionDao.findAll.foreach(d => {
      val pos = ids.indexOf(d.id.toString)
      d.position = pos
      deliveryOptionDao.save(d)
    })
    "ok"
  }

  @ModelAttribute("options") def options = deliveryOptionDao.findAll.sortBy(_.position).asJava
}
