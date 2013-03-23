package com.liferay.scalapress.plugin.profile.tag

import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.security.SecurityFuncs
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel
  *
  *         Shows the current logged in users username or nothing
  *
  * */
@Tag("account_username")
class UsernameTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        SecurityFuncs.getUserDetails(request.request).map(_.username)
    }
}

/*Makes a link to the accounts page, or if the user is not logged in then shows a login link */
@Tag("account_link")
class AccountLinkTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val label = params.get("label").orElse(params.get("text"))
        val link = SecurityFuncs.hasUserRole(request.request) match {
            case true => super.buildLink("/account", label.getOrElse("Account"), params)
            case false => super.buildLink("/login", label.getOrElse("Login or Register"), params)
        }
        Some(link)
    }
}