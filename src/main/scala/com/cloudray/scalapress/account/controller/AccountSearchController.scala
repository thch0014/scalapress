package com.cloudray.scalapress.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.sksamuel.scoot.soa.Paging
import org.springframework.ui.ModelMap
import javax.servlet.http.HttpServletRequest
import scala.beans.BeanProperty
import com.cloudray.scalapress.obj.controller.admin.AccountStatusPopulator
import com.cloudray.scalapress.account.{AccountQuery, AccountTypeDao, AccountDao, Account}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/account"))
@Autowired
class AccountSearchController(accountDao: AccountDao,
                              accountTypeDao: AccountTypeDao,
                              context: ScalapressContext) extends AccountStatusPopulator {

  @RequestMapping
  def search(@ModelAttribute("form") form: SearchForm,
             @RequestParam(value = "accountTypeId") accountTypeId: Long,
             @RequestParam(value = "pageNumber", defaultValue = "1") pageNumber: Int,
             model: ModelMap,
             req: HttpServletRequest) = {

    val query = new AccountQuery
    query.pageNumber = pageNumber
    query.pageSize = 20
    query.status = Option(form.status)
    query.name = Option(form.name)
    query.accountTypeId = Option(accountTypeId).filter(_ > 0)

    val page = accountDao.search(query)
    model.put("accounts", page.java)
    model.put("paging", Paging(req, page))

    "admin/account/list.vm"
  }

  @RequestMapping(value = Array("create"), params = Array("accountTypeId", "name"))
  def create(@RequestParam("accountTypeId") typeId: java.lang.Long, @RequestParam("name") name: String): String = {

    val t = accountTypeDao.find(typeId)
    val acc = Account(t)
    acc.name = name
    accountDao.save(acc)

    "redirect:/backoffice/account/" + acc.id
  }

  @ModelAttribute("accountType")
  def types(@RequestParam("accountTypeId") accountTypeId: Long) = accountTypeDao.find(accountTypeId)

  @ModelAttribute("form")
  def form = new SearchForm

}

class SearchForm {
  @BeanProperty var status: String = Account.STATUS_ACTIVE
  @BeanProperty var name: String = _
}
