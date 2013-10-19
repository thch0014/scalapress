package com.cloudray.scalapress.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import scala.Array
import com.cloudray.scalapress.ScalapressContext
import org.springframework.ui.ModelMap
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.account.{AccountType, AccountTypeDao}
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/accounttype/{id}"))
@Autowired
class AccountTypeEditController(accountTypeDao: AccountTypeDao,
                                markupDao: MarkupDao,
                                sectionDao: SectionDao,
                                context: ScalapressContext) {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute("accountType") t: AccountType) = "admin/account/type/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute("accountType") t: AccountType) = {
    accountTypeDao.save(t)
    edit(t)
  }

  @ModelAttribute("accountType")
  def accountType(@PathVariable("id") id: java.lang.Long, model: ModelMap) = accountTypeDao.find(id)
}
