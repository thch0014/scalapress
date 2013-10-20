package com.cloudray.scalapress.account.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import com.cloudray.scalapress.account.{AccountType, AccountTypeDao}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("/backoffice/accounttype"))
@Autowired
class AccountTypeListController(accountTypeDao: AccountTypeDao) {

  @RequestMapping(produces = Array("text/html"))
  def list = "admin/account/type/list.vm"

  @RequestMapping(produces = Array("text/html"), value = Array("{accountTypeId}/delete"))
  def delete(@PathVariable("accountTypeId") accountTypeId: Long) = {
    val accountType = accountTypeDao.find(accountTypeId)
    accountTypeDao.save(accountType)
    list
  }

  @RequestMapping(value = Array("create"), produces = Array("text/html"), params = Array("name"))
  def create(@RequestParam("name") name: String) = {
    val accountType = new AccountType
    accountType.name = name
    accountTypeDao.save(accountType)
    "redirect:/backoffice/accounttype"
  }

  @ModelAttribute("accountTypes") def types = accountTypeDao.findAll().sortBy(_.id).asJava
}
