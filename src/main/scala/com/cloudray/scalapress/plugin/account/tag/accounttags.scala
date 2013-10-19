package com.cloudray.scalapress.plugin.account.tag

import com.cloudray.scalapress.{Tag, ScalapressRequest}
import com.cloudray.scalapress.security.{SecurityResolver, SpringSecurityResolver}
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel  **/
//Shows the current logged in users username or nothing
@Tag("account_username")
class UsernameTag extends ScalapressTag with TagBuilder {
  var securityResolver: SecurityResolver = SpringSecurityResolver
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    securityResolver.getAccount(request.request).map(_.name).map(arg => super.build(arg, params))
  }
}

/*Makes a link to the accounts page, or if the user is not logged in then shows a login link */
@Tag("account_link")
class AccountLinkTag extends ScalapressTag with TagBuilder {
  var securityResolver: SecurityResolver = SpringSecurityResolver
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val label = params.get("label").orElse(params.get("text"))
    val link = securityResolver.hasUserRole(request.request) match {
      case true => super.buildLink("/account", label.getOrElse("Account"), params)
      case false => super.buildLink("/login", label.getOrElse("Login or Register"), params)
    }
    Some(link)
  }
}