package com.cloudray.scalapress.account.controller

import com.cloudray.scalapress.settings.Installation
import com.cloudray.scalapress.util.Scalate

/** @author Stephen Samuel */
class DefaultRegistrationCompletionRenderer(installation: Installation) {

  val RESOURCE = "/com/cloudray/scalapress/account/regcompleted.ssp"

  def render: String = Scalate.layout(RESOURCE, Map("siteName" -> installation.name))
}
