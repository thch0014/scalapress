package com.cloudray.scalapress.account.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ModelAttribute, RequestMethod, RequestMapping}
import com.cloudray.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.ModelMap
import org.springframework.security.authentication.encoding.PasswordEncoder
import scala.beans.BeanProperty
import com.cloudray.scalapress.account.{AccountDao, Account}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/account/{id}"))
class AccountEditController(accountDao: AccountDao,
                            passwordEncoder: PasswordEncoder,
                            context: ScalapressContext) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("form") form: EditForm): String = "admin/object/edit.vm"

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

  @ModelAttribute("installation") def installation = context.installationDao.get

}

class EditForm {
  @BeanProperty var account: Account = _
  @BeanProperty var changePassword: String = _
}

