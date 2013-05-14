package com.liferay.scalapress.plugin.profile.controller

import com.liferay.scalapress.plugin.profile.AccountLink

/** @author Stephen Samuel */
object AccountRenderer {
    def links(links: Seq[Class[AccountLink]]) = {
        links.map(klass => klass.newInstance)
          .map(link => {
            <a href={link.profilePageLinkUrl} class="btn btn-info" id={link.profilePageLinkId}>
                {link.profilePageLinkText}
            </a>
        })
    }
}
