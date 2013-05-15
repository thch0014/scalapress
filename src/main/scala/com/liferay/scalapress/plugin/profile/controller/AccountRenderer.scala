package com.liferay.scalapress.plugin.profile.controller

import com.liferay.scalapress.plugin.profile.AccountLink
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
object AccountRenderer {
    def links(links: Seq[Class[AccountLink]], context: ScalapressContext): String = {

        val more = links :+ classOf[LogoutAccountLink]

        more.map(klass => klass.newInstance)
          .filter(_.accountLinkEnabled(context))
          .map(link => {
            <a href={link.profilePageLinkUrl} class="btn btn-info" id={link.profilePageLinkId}>
                {link.profilePageLinkText}
            </a>
        }).mkString("<br/>")
    }
}

class LogoutAccountLink extends AccountLink {
    def accountLinkEnabled(context: ScalapressContext): Boolean = true
    def profilePageLinkId: String = "accountlink-logout"
    def profilePageLinkUrl: String = "/j_spring_security_logout"
    def profilePageLinkText: String = "Logout"
}