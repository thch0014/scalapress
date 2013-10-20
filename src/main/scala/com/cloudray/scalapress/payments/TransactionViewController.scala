package com.cloudray.scalapress.payments

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping}
import org.springframework.ui.ModelMap
import org.springframework.stereotype.Controller

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/tx/{id}"))
class TransactionViewController(transactionDao: TransactionDao) {

  @RequestMapping
  def search(@PathVariable("id") id: Long, model: ModelMap): String = {

    val tx = transactionDao.find(id)
    model.put("tx", tx)
    "admin/tx/view.vm"
  }
}
