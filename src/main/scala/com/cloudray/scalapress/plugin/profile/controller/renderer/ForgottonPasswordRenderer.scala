package com.cloudray.scalapress.plugin.profile.controller.renderer

import org.fusesource.scalate.TemplateEngine

/** @author Stephen Samuel */
object ForgottonPasswordRenderer {
  val engine = new TemplateEngine
  def resetFail = engine.layout("/com/cloudray/scalapress/plugin/profile/controller/reset_fail.ssp")
  def resetSuccess = engine.layout("/com/cloudray/scalapress/plugin/profile/controller/reset_success.ssp")
  def renderRequest = engine.layout("/com/cloudray/scalapress/plugin/profile/controller/request.ssp")
  def renderSubmissionConf = engine.layout("/com/cloudray/scalapress/plugin/profile/controller/passwordconf.ssp")
}
