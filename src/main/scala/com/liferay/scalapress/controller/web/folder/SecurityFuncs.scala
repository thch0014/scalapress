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

    def hasAdminRole(request: HttpServletRequest) = SecurityContextHolder
      .getContext
      .getAuthentication
      .getAuthorities
      .contains(AdminAuthority)

    def getAccount(request: HttpServletRequest): Option[Obj] =
        Option(SecurityContextHolder
          .getContext
          .getAuthentication
          .getPrincipal
          .asInstanceOf[ObjectUserDetails].user)

}
