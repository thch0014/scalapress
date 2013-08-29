package com.cloudray.scalapress.plugin.ecommerce

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.ecommerce.controller.admin.OrderQuery

/** @author Stephen Samuel */
@Component
class SalesReporter {

  @Autowired var orderDao: OrderDao = _

  def generate(status: String, start: Long, end: Long): Seq[ReportLine] = {

    val q = new OrderQuery
    q.from = Some(start)
    q.to = Some(end)
    q.status = Option(status)
    q.pageSize = 10000000
    val orders = orderDao.search(q).results

    orders.map(
      order => ReportLine(order.id.toString,
        order.datePlaced,
        order.account.name,
        order.account.email,
        order.status,
        order.subtotal,
        order.vat,
        order.total,
        order.customerNote))
  }
}

case class ReportLine(orderId: String,
                      datePlaced: Long,
                      name: String,
                      email: String,
                      status: String,
                      subtotal: Double,
                      vat: Double,
                      total: Double,
                      note: String)