package com.cloudray.scalapress.payments

import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import com.googlecode.genericdao.search.Search
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/tx"))
@Autowired
class TransactionListController(context: ScalapressContext) {

  @RequestMapping
  def search(@RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int,
             model: ModelMap, req: HttpServletRequest): String = {

    val search = new Search(classOf[Transaction])
    search.setMaxResults(50)
    search.addSort("id", true)
    val results = context.transactionDao.search(search)

    model.put("results", results.asJava)
    //  model.put("paging", Paging(req, orders))
    "admin/tx/list.vm"
  }
}
