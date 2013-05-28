package com.cloudray.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import com.sksamuel.scoot.soa.Paging
import reflect.BeanProperty
import com.cloudray.scalapress.plugin.ecommerce.{OrderDao, ShoppingPluginDao}
import com.cloudray.scalapress.obj.controller.admin.OrderStatusPopulator
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/order"))
class OrderSearchController extends OrderStatusPopulator {

    @Autowired var shoppingPluginDao: ShoppingPluginDao = _
    @Autowired var orderDao: OrderDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping
    def search(@ModelAttribute("form") form: SearchForm,
               @RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int,
               model: ModelMap,
               req: HttpServletRequest): String = {

        Option(form.orderId).filter(_.trim.length > 0).map(_.toLong).flatMap(id => Option(orderDao.find(id))) match {
            case Some(order) =>
                "redirect:/backoffice/order/" + order.id

            case None =>

                val query = new OrderQuery
                query.pageNumber = pageNumber
                query.pageSize = 20
                query.status = Option(form.status)
                query.name = Option(form.name)
                query.orderId = Option(form.orderId)

                val orders = orderDao.search(query)
                model.put("orders", orders.java)
                model.put("paging", Paging(req, orders))
                "admin/order/list.vm"
        }
    }

    @RequestMapping(value = Array("create"), produces = Array("text/html"))
    def create(req: HttpServletRequest, @RequestParam(value = "accountId", defaultValue = "0") accountId: Long) = {
        val account = accountId match {
            case 0 =>
                val account = Obj(context.typeDao.getAccount.get)
                account.status = "Disabled"
                account.name = "New Account"
                context.objectDao.save(account)
                account
            case _ => context.objectDao.find(accountId)
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