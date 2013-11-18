package com.cloudray.scalapress.plugin.ecommerce.shopping.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestBody, RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import scala.collection.JavaConverters._
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.DeliveryOptionDao
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.DeliveryOption
import javax.servlet.http.HttpServletResponse

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/delivery"))
class DeliveryOptionListController(deliveryOptionDao: DeliveryOptionDao) {

  @RequestMapping
  def list = "admin/plugin/ecommerce/shopping/delivery/list.vm"

  @RequestMapping(value = Array("create"))
  def create = {
    val d = new DeliveryOption
    deliveryOptionDao.save(d)
    "redirect:/backoffice/delivery"
  }

  @ResponseBody
  @RequestMapping(value = Array("/order"), method = Array(RequestMethod.POST))
  def reorder(@RequestBody order: String, resp: HttpServletResponse) {

    val ids = order.split("-")
    deliveryOptionDao.findAll.foreach(d => {
      val pos = ids.indexOf(d.id.toString)
      d.position = pos
      deliveryOptionDao.save(d)
    })
    resp.setStatus(HttpServletResponse.SC_OK)
  }

  @ModelAttribute("options") def options = deliveryOptionDao.findAll.sortBy(_.position).asJava
}
