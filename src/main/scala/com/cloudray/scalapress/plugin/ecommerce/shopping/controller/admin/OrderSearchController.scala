package com.cloudray.scalapress.plugin.ecommerce.shopping.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import com.sksamuel.scoot.soa.Paging
import com.cloudray.scalapress.item.controller.admin.OrderStatusPopulator
import scala.beans.BeanProperty
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.{OrderDao, OrderQuery}
import com.cloudray.scalapress.plugin.ecommerce.shopping.domain.ShoppingPluginDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/order"))
class OrderSearchController(var shoppingPluginDao: ShoppingPluginDao,
                            orderDao: OrderDao,
                            context: ScalapressContext) extends OrderStatusPopulator {

  @RequestMapping
  def search(@ModelAttribute("form") form: SearchForm,
             @RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int,
             model: ModelMap,
             req: HttpServletRequest): String = {

    val order = Option(form.orderId)
      .map(_.replaceAll("\\D", ""))
      .filterNot(_.isEmpty)
      .map(_.toLong)
      .flatMap(id => Option(orderDao.find(id)))

    order match {
      case Some(o) => "redirect:/backoffice/order/" + o.id
      case None =>

        val query = new OrderQuery
        query.pageNumber = pageNumber
        query.pageSize = 20
        query.status = Option(form.status)
        query.name = Option(form.name)
        query.orderId = Option(form.orderId)

        val orders = orderDao.search(query)
        if (orders != null) {
          model.put("orders", orders.java)
          model.put("paging", Paging(req, orders))
        }
        "admin/plugin/shopping/order/list.vm"
    }
  }

  @RequestMapping(value = Array("create"), produces = Array("text/html"))
  def create(req: HttpServletRequest, @RequestParam(value = "accountId", defaultValue = "0") accountId: Long) = {
    val account = accountId match {
      case 0 =>
        val account = Account(context.accountTypeDao.default)
        account.status = Account.STATUS_ACTIVE
        account.name = "New Account"
        context.accountDao.save(account)
        account
      case _ => context.accountDao.find(accountId)
    }
    val u = Order(req.getRemoteAddr, account)
    orderDao.save(u)
    "redirect:/backoffice/order"
  }

  @ModelAttribute("form") def form = new SearchForm()
}

class SearchForm {
  @BeanProperty var orderId: String = _
  @BeanProperty var status: String = _
  @BeanProperty var name: String = _
}