package com.cloudray.scalapress.plugin.ecommerce.shopping

import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.OrderDao

/** @author Stephen Samuel */
class EmailReportService(dao: OrderDao) {
  def emails: Seq[String] = dao.emails
  def csv: String = emails.mkString("\n")
}
