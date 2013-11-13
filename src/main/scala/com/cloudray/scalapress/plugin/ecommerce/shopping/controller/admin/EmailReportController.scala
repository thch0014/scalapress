package com.cloudray.scalapress.plugin.ecommerce.shopping.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletResponse
import com.cloudray.scalapress.plugin.ecommerce.shopping.EmailReportService
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.OrderDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/ecommerce/report/email"))
class EmailReportController(dao: OrderDao) {

  @RequestMapping(produces = Array("text/plain"))
  @ResponseBody
  def report(response: HttpServletResponse): String = {
    response.setHeader("content-disposition", "attachment; filename=\"emails.csv\"")
    val body = new EmailReportService(dao).csv
    body
  }
}
