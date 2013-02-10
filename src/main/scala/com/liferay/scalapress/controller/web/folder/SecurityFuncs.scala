package com.liferay.scalapress.controller.web.folder

import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import com.liferay.scalapress.service.security.{ObjectUserDetails, AdminAuthority}
import com.liferay.scalapress.domain.Obj

/** @author Stephen Samuel */
object SecurityFuncs {

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
