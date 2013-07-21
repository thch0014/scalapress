package com.cloudray.scalapress.plugin.cookieconsent

import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.{ScalapressRequest, Tag}
import com.cloudray.scalapress.util.Scalate

@Tag("cookiecontent")
class CookieConsentTag extends ScalapressTag {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    Some(Scalate.layout("/com/cloudray/scalapress/plugin/cookieconsent/script.ssp"))
  }
}