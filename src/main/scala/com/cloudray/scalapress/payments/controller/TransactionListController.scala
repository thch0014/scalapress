package com.cloudray.scalapress.payments.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import com.googlecode.genericdao.search.Search
import scala.collection.JavaConverters._
import com.cloudray.scalapress.payments.{Transaction, TransactionDao}

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/tx"))
class TransactionListController(transactionDao: TransactionDao) {

  @RequestMapping
  def search(@RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int,
             model: ModelMap, req: HttpServletRequest): String = {

    val search = new Search(classOf[Transaction])
    search.setMaxResults(50)
    search.addSort("id", true)
    val results = transactionDao.search(search)

    model.put("results", results.asJava)
    //model.put("paging", Paging(req, results))
    "admin/tx/list.vm"
  }
}
