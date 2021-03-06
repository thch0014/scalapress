package com.cloudray.scalapress.security

import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.Authentication
import com.cloudray.scalapress.account.Account

/** @author Stephen Samuel */
object SpringSecurityResolver extends SecurityResolver {

  def getAuth(req: HttpServletRequest): Option[Authentication] =
    Option(SecurityContextHolder.getContext.getAuthentication)

  def getUserDetails(req: HttpServletRequest): Option[ScalaPressUserDetails] =
    getAuth(req)
      .map(_.getPrincipal)
      .filter(_.isInstanceOf[ScalaPressUserDetails])
      .map(_.asInstanceOf[ScalaPressUserDetails])

  def getAdminDetails(req: HttpServletRequest): AdminUserDetails =
    getUserDetails(req).get.asInstanceOf[AdminUserDetails]

  def getAccount(req: HttpServletRequest): Option[Account] = getUserDetails(req).flatMap(arg => Option(arg.account))

  def hasAdminRole(req: HttpServletRequest): Boolean =
    getAuth(req).exists(_.getAuthorities.contains(AdminAuthority))

  def hasUserRole(req: HttpServletRequest): Boolean =
    getAuth(req).exists(_.getAuthorities.contains(UserAuthority))
}

trait SecurityResolver {
  def hasUserRole(req: HttpServletRequest): Boolean
  def getAccount(req: HttpServletRequest): Option[Account]
}
