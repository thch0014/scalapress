package com.cloudray.scalapress.account.controller.renderer

import org.springframework.validation.Errors
import com.cloudray.scalapress.account.controller.RegistrationForm
import com.cloudray.scalapress.account.AccountPlugin
import com.cloudray.scalapress.util.Scalate
import com.cloudray.scalapress.settings.Installation
import scala.xml.Unparsed

/** @author Stephen Samuel */
class RegistrationRenderer(installation: Installation) {

  def renderChooseAccountType(plugin: AccountPlugin) = <div class="container-fluid">Choose Account Type</div>

  def renderRegistrationPage(form: RegistrationForm, plugin: AccountPlugin, errors: Errors) = {

    val nameError = Option(errors.getFieldError("name")).map(_.getDefaultMessage).getOrElse("")
    val emailError = Option(errors.getFieldError("email")).map(_.getDefaultMessage).getOrElse("")
    val passwordError = Option(errors.getFieldError("password")).map(_.getDefaultMessage).getOrElse("")

    val nameStyle = if (errors.hasFieldErrors("name")) "error" else ""
    val emailStyle = if (errors.hasFieldErrors("email")) "error" else ""
    val passwordStyle = if (errors.hasFieldErrors("password")) "error" else ""

    Scalate.layout("/com/cloudray/scalapress/account/register.ssp",
      Map("nameError" -> nameError,
        "emailError" -> Unparsed(emailError),
        "passwordError" -> passwordError,
        "name" -> Option(form.name).getOrElse(""),
        "email" -> Option(form.email).getOrElse(""),
        "siteName" -> Option(installation.name).getOrElse(""),
        "nameStyle" -> nameStyle,
        "emailStyle" -> emailStyle,
        "passwordStyle" -> passwordStyle))
  }
}
