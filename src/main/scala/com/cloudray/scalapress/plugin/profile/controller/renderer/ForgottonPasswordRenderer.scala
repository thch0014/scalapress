package com.cloudray.scalapress.plugin.profile.controller.renderer

import com.cloudray.scalapress.util.Scalate

/** @author Stephen Samuel */
object ForgottonPasswordRenderer {
  def resetFail = Scalate.layout("/com/cloudray/scalapress/plugin/profile/controller/reset_fail.ssp")
  def resetSuccess = Scalate.layout("/com/cloudray/scalapress/plugin/profile/controller/reset_success.ssp")
  def renderRequest = Scalate.layout("/com/cloudray/scalapress/plugin/profile/controller/request.ssp")
  def renderSubmissionConf = Scalate.layout("/com/cloudray/scalapress/plugin/profile/controller/passwordconf.ssp")
}
