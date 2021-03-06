package com.cloudray.scalapress.account.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import org.springframework.security.authentication.encoding.PasswordEncoder
import scala.beans.BeanProperty
import com.cloudray.scalapress.account.{AccountDao, Account}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.item.controller.admin.AccountStatusPopulator
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.{AddressDao, OrderQuery}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/account/{id}"))
class AccountEditController(accountDao: AccountDao,
                            addressDao: AddressDao,
                            passwordEncoder: PasswordEncoder,
                            context: ScalapressContext) extends AccountStatusPopulator {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("form") form: EditForm): String = "admin/account/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("form") form: EditForm, req: HttpServletRequest): String = {

    Option(form.changePassword).map(_.trim).filterNot(_.isEmpty).foreach(pass => {
      form.account.passwordHash = passwordEncoder.encodePassword(pass, null)
    })

    accountDao.save(form.account)
    "redirect:/backoffice/account/" + form.account.id
  }

  @ModelAttribute("form")
  def form(@PathVariable("id") id: Long, model: ModelMap): EditForm = {
    val account = accountDao.find(id)
    val form = new EditForm
    form.account = account
    form
  }

  @ModelAttribute("installation")
  def installation = context.installationDao.get

  @ModelAttribute("orders")
  def orders(@PathVariable("id") id: Long) = context.orderDao.search(new OrderQuery().withAccountId(id)).java

  @ModelAttribute("addresses")
  def addresses(@PathVariable("id") id: Long) = context.bean[AddressDao].findFromAccountId(id).asJava
}

class EditForm {
  @BeanProperty var account: Account = _
  @BeanProperty var changePassword: String = _
}

