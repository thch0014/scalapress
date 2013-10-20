package com.cloudray.scalapress.plugin.account.controller.renderer

import org.springframework.validation.Errors
import com.cloudray.scalapress.account.controller.RegistrationForm
import com.cloudray.scalapress.account.AccountPlugin

/** @author Stephen Samuel */
object RegistrationRenderer {

  def renderChooseAccountType(plugin: AccountPlugin) =
    <div class="container-fluid">Choose Account Type
    </div>

  def renderRegistrationPage(form: RegistrationForm, plugin: AccountPlugin, errors: Errors) = {
    <div class="registration">
      <form class="form-horizontal registration" method="POST">
        <div class="control-group">
          <label for="name">
            Your name
          </label>
          <input name="name" type="text" placeholder="Your name" value={form.name}/>
          <span class="help-inline">
            {Option(errors.getFieldError("name")).map(_.getDefaultMessage).getOrElse("")}
          </span>
        </div>
        <div class="control-group">
          <label for="email">
            Email
          </label>
          <input name="email" type="email" placeholder="Email Address" value={form.email}/>
          <span class="help-inline">
            {Option(errors.getFieldError("email")).map(_.getDefaultMessage).getOrElse("")}
          </span>
        </div>
        <div class="control-group">
          <label for="password">
            Password
          </label>
          <input name="password" type="password" placeholder="Password"/>
          <span class="help-inline">
            {Option(errors.getFieldError("password")).map(_.getDefaultMessage).getOrElse("")}
          </span>
        </div>
        <button type="submit" class="btn">Register</button>
      </form>
    </div>
  }
}
