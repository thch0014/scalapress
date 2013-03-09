package com.liferay.scalapress.controller.web.folder

import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import com.liferay.scalapress.service.security.{BasicUserDetails, ObjectUserDetails, AdminAuthority}
import com.liferay.scalapress.domain.Obj

/** @author Stephen Samuel */
object SecurityFuncs {

    def getUser(request: HttpServletRequest) = {
        SecurityContextHolder
          .getContext
          .getAuthentication
          .getPrincipal
          .asInstanceOf[BasicUserDetails].user
    }

    def hasAdminRole(request: HttpServletRequest) = {
        Option(SecurityContextHolder.getContext.getAuthentication)
          .map(_.getAuthorities.contains(AdminAuthority))
          .getOrElse(false)
    }

    def getAccount(request: HttpServletRequest): Option[Obj] =
        Option(SecurityContextHolder
          .getContext
          .getAuthentication
          .getPrincipal
          .asInstanceOf[ObjectUserDetails].user)

}
