package com.cloudray.scalapress.plugin.ecommerce

/** @author Stephen Samuel */
class EmailReportService(dao: OrderDao) {
  def emails: Seq[String] = dao.emails
  def csv: String = emails.mkString("\n")
}
