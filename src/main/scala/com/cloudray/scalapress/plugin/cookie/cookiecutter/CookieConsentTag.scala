package com.cloudray.scalapress.plugin.cookie.cookiecutter

import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.util.Scalate
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

@Tag("cookiecontent")
class CookieConsentTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    Some(Scalate.layout("/com/cloudray/scalapress/plugin/cookiecutter/script.ssp"))
  }
}