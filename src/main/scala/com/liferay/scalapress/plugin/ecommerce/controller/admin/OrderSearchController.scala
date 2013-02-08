package com.liferay.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.OrderDao
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import javax.servlet.http.HttpServletRequest
import com.googlecode.genericdao.search.Search

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/order"))
class OrderSearchController {

    @Autowired var orderDao: OrderDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/order/list.vm"

    @RequestMapping(value = Array("create"), produces = Array("text/html"))
    def create(req: HttpServletRequest) = {
        val u = Order(req.getRemoteAddr)
        orderDao.save(u)
        "redirect:/backoffice/order"
    }

    import scala.collection.JavaConverters._

    @ModelAttribute("orders") def orders = {
        orderDao.search(new Search(classOf[Order]).addSort("id", true).setMaxResults(200)).asJava
    }

}
