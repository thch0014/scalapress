package com.cloudray.scalapress.account.controller.renderer

import org.springframework.validation.Errors
import com.cloudray.scalapress.account.controller.Profile
import com.cloudray.scalapress.util.Scalate

/** @author Stephen Samuel */
class ProfileRenderer {

  def render(profile: Profile, errors: Errors) {

    val nameError = Option(errors.getFieldError("name")).map(_.getDefaultMessage).getOrElse("")
    val emailError = Option(errors.getFieldError("email")).map(_.getDefaultMessage).getOrElse("")
    val passwordError = Option(errors.getFieldError("password")).map(_.getDefaultMessage).getOrElse("")

    Scalate.layout("/com/cloudray/scalapress/account/profile.ssp",
      Map("name" -> profile.name,
        "email" -> profile.email,
        "nameError" -> nameError,
        "emailError" -> emailError,
        "passwordError" -> passwordError)
    )
  }
}
