package com.cloudray.scalapress.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping, ResponseBody}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import com.cloudray.scalapress.account.AccountDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("rest/account"))
class AccountRestController(accountDao: AccountDao) {

  @ResponseBody
  @RequestMapping(produces = Array(MediaType.APPLICATION_JSON_VALUE))
  def typeAhead(@RequestParam("q") q: String): Array[Datum] = accountDao.typeAhead(q)
}

case class Datum(value: String, id: String)
//
//@Component
//@Autowired
//@Path("rest/account")
//class AccountRestServlet(accountDao: AccountDao) extends SpringJsonScalatraServlet {
//
//  get("/:id") {
//    accountDao.find(params("id").toLong)
//  }
//}