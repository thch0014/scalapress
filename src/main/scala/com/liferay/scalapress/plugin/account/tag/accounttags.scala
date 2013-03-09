package com.liferay.scalapress.plugin.account.tag

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import com.liferay.scalapress.security.ObjectUserDetails

/** @author Stephen Samuel */
object UsernameTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        Option(request.request.getUserPrincipal)
          .map(_.asInstanceOf[UsernamePasswordAuthenticationToken])
          .map(_.getPrincipal)
          .map(_.asInstanceOf[ObjectUserDetails])
          .map(_.user.name)
    }
}

object AccountLinkTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        None
    }
}