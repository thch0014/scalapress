package com.cloudray.scalapress.plugin.profile.controller

import com.cloudray.scalapress.plugin.profile.AccountLink
import com.cloudray.scalapress.ScalapressContext

/** @author Stephen Samuel */
object AccountRenderer {
    def links(links: Seq[Class[_ <: AccountLink]], context: ScalapressContext): String = {

        links.map(klass => klass.newInstance)
          .sortBy(_.accountLinkPriority)
          .filter(_.accountLinkEnabled(context))
          .map(link => {
            <div class="row-fluid">
                <div class="span9">
                    {link.accountLinkText}
                </div>
                <div class="span3">
                    <a href={link.profilePageLinkUrl} class="btn btn-block" id={link.profilePageLinkId}>
                        {link.profilePageLinkText}
                    </a>
                </div>
            </div>

        }).mkString("<br/>")
    }
}

class LogoutAccountLink extends AccountLink {
    def accountLinkEnabled(context: ScalapressContext): Boolean = true
    def profilePageLinkId: String = "accountlink-logout"
    def profilePageLinkUrl: String = "/j_spring_security_logout"
    def profilePageLinkText: String = "Logout"
    def accountLinkText: String = "Logout of your account"
    override def accountLinkPriority: Int = Integer.MAX_VALUE
}