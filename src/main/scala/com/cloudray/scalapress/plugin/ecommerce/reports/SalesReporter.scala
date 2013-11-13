package com.cloudray.scalapress.plugin.ecommerce.reports

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.{OrderQuery, OrderDao}

/** @author Stephen Samuel */
@Component
@Autowired
class SalesReporter(orderDao: OrderDao) {

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
        Option(order.customerNote),
        order.descriptions))
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
                      note: Option[String],
                      details: Seq[String])