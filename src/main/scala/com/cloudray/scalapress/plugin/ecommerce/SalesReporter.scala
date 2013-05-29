package com.cloudray.scalapress.plugin.ecommerce

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import org.joda.time.DateMidnight
import com.cloudray.scalapress.plugin.ecommerce.controller.admin.OrderQuery

/** @author Stephen Samuel */
@Component
class SalesReporter {

    @Autowired var orderDao: OrderDao = _

    def generate(start: DateMidnight, end: DateMidnight): Seq[ReportLine] = {

        val q = new OrderQuery
        q.from = Some(start.getMillis)
        q.to = Some(end.plusDays(1).getMillis)
        q.pageSize = 10000000
        val orders = orderDao.search(q).results

        orders.map(order => ReportLine(order.id.toString, order.datePlaced, order.status, order.subtotal, order.vat, order.total))
    }
}

case class ReportLine(orderId: String, datePlaced: Long, status: String, subtotal: Double, vat: Double, total: Double)