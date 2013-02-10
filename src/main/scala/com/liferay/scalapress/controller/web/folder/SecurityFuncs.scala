package com.liferay.scalapress.controller.web.folder

import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import com.liferay.scalapress.service.security.AdminAuthority

/** @author Stephen Samuel */
object SecurityFuncs {

    def hasAdminRole(request: HttpServletRequest) = SecurityContextHolder
      .getContext
      .getAuthentication
      .getAuthorities
      .contains(AdminAuthority)

}
