package com.cloudray.scalapress.plugin.profile.controller

import org.fusesource.scalate.TemplateEngine

/** @author Stephen Samuel */
object ForgottonPasswordRenderer {

  val engine = new TemplateEngine

  def resetFail = {
    <div class="forgotton-password-request">
      We were unable to reset your password. Please try again.
    </div>
  }

  def resetSuccess = {
    <div class="forgotton-password-request">
      Your password has been reset. An email has been sent to your address which contains your new password. You should login and change the password at your earliest opportunity.
    </div>
  }

  def renderRequest = {
    <div class="forgotton-password-request">
      <form class="form-horizontal registration" method="POST">
        <div class="control-group">
          <label for="Email">
            Account Email
          </label>
          <input name="email" type="text" placeholder="email"/>
        </div>
        <button type="submit" class="btn">Request New Password</button>
      </form>
    </div>
  }

  def renderSubmissionConf = engine.layout("/com/cloudray/scalapress/plugin/profile/controller/passwordconf.ssp")
}
