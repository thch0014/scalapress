package com.cloudray.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestParam, PathVariable, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.domain.{Address, DeliveryOption, OrderLine, OrderComment, Order}
import com.cloudray.scalapress.plugin.ecommerce.{OrderCustomerNotificationService, OrderDao, ShoppingPluginDao}
import scala.collection.JavaConverters._
import com.cloudray.scalapress.plugin.ecommerce.dao.{AddressDao, DeliveryOptionDao}
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.security.SpringSecurityResolver
import com.cloudray.scalapress.obj.ObjectDao
import com.cloudray.scalapress.obj.controller.admin.{AddressPopulator, DeliveryOptionPopulator, OrderStatusPopulator}
import org.joda.time.{DateTimeZone, DateTime}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/order/{id}"))
class OrderEditController extends OrderStatusPopulator with DeliveryOptionPopulator with AddressPopulator {

  @Autowired var orderDao: OrderDao = _
  @Autowired var addressDao: AddressDao = _
  @Autowired var deliveryOptionDao: DeliveryOptionDao = _
  @Autowired var objectDao: ObjectDao = _
  @Autowired var context: ScalapressContext = _
  @Autowired var shoppingPluginDao: ShoppingPluginDao = _
  @Autowired var notificationService: OrderCustomerNotificationService = _

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute order: Order) = "admin/plugin/shopping/order/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute order: Order,
           @ModelAttribute("form") form: OrderEditForm,
           req: HttpServletRequest) = {

    order.status match {
      case Order.STATUS_COMPLETED =>
        notificationService.orderCompleted(order)
      case _ =>

        if (form.changeDeliveryCharge > 0) {
          order.deliveryCharge = (form.changeDeliveryCharge * 100.0).toInt
        }

        if (form.changeDeliveryOption != null) {
          order.deliveryCharge = form.changeDeliveryOption.charge
          order.deliveryVatRate = form.changeDeliveryOption.vatRate
          order.deliveryDetails = form.changeDeliveryOption.name
        }

        if (form.changeBillingAddress != null) {
          order.billingAddress = form.changeBillingAddress
        }

        if (form.changeDeliveryAddress != null) {
          order.deliveryAddress = form.changeDeliveryAddress
        }

        order.lines.asScala.foreach(line => {
          val qty = req.getParameter("lineQty" + line.id).toInt
          line.qty = if (qty < 1) 1 else qty
        })

        order.lines.asScala.foreach(line => {
          val p = (req.getParameter("linePrice" + line.id).toDouble * 100.0).toInt
          line.price = p
        })
    }

    orderDao.save(order)
    "redirect:/backoffice/order/" + order.id
  }

  @RequestMapping(value = Array("line/add"), params = Array("objId"))
  def addLine(@ModelAttribute order: Order, @RequestParam("objId") id: Long) = {
    val obj = objectDao.find(id)
    if (obj != null) {
      val line = OrderLine(obj)
      line.order = order
      order.lines.add(line)
      orderDao.save(order)
    }
    "redirect:/backoffice/order/" + order.id
  }

  @RequestMapping(value = Array("line/add"), params = Array("desc", "price"))
  def addLine(@ModelAttribute order: Order,
              @RequestParam("desc") desc: String,
              @RequestParam("price") price: Double) = {

    val line = OrderLine(desc, (price * 100).toInt)
    line.order = order
    order.lines.add(line)
    orderDao.save(order)

    "redirect:/backoffice/order/" + order.id
  }

  @RequestMapping(value = Array("line/{lineId}/remove"))
  def removeLine(@ModelAttribute order: Order, @PathVariable("lineId") id: Long) = {
    order.lines.asScala.find(_.id == id) match {
      case Some(line) =>
        order.lines.remove(line)
        line.order = null
        orderDao.save(order)
      case _ =>
    }
    "redirect:/backoffice/order/" + order.id
  }

  @RequestMapping(value = Array("comment/add"), method = Array(RequestMethod.POST))
  def addComment(req: HttpServletRequest, @RequestParam("message") message: String, @ModelAttribute order: Order) = {
    val comment = new OrderComment
    comment.date = new DateTime(DateTimeZone.UTC).getMillis
    comment.author = SpringSecurityResolver.getUserDetails(req).get.username
    comment.body = message
    comment.order = order
    order.comments.add(comment)
    orderDao.save(order)
    "redirect:/backoffice/order/" + order.id
  }

  @ModelAttribute("form") def form = new OrderEditForm

  @ModelAttribute def order(@PathVariable("id") id: Long, model: ModelMap) = {

    val order = orderDao.find(id)
    order.status = Option(order.status).map(_.trim).getOrElse(null)

    addressOptions(order.account.id, model)
    order
  }
}

class OrderEditForm {
  @BeanProperty var changeDeliveryCharge: Double = _
  @BeanProperty var changeBillingAddress: Address = _
  @BeanProperty var changeDeliveryAddress: Address = _
  @BeanProperty var changeDeliveryOption: DeliveryOption = _
}
