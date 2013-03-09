package com.liferay.scalapress.security

import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.Authentication

/** @author Stephen Samuel */
object SecurityFuncs {

    def getAuth(req: HttpServletRequest): Option[Authentication] =
        Option(SecurityContextHolder.getContext.getAuthentication)

    def getUserDetails(req: HttpServletRequest): Option[ScalaPressUserDetails] =
        getAuth(req).map(_.asInstanceOf[ScalaPressUserDetails])

    def hasAdminRole(req: HttpServletRequest): Boolean =
        getAuth(req).exists(_.getAuthorities.contains(AdminAuthority))

    def hasUserRole(req: HttpServletRequest): Boolean =
        getAuth(req).exists(_.getAuthorities.contains(UserAuthority))

}
