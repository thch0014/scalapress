package com.cloudray.scalapress.security

import org.springframework.security.core.userdetails.{UsernameNotFoundException, UserDetails, UserDetailsService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.user.{User, UserDao}
import com.cloudray.scalapress.account.Account

/** @author Stephen Samuel */
@Component
class AdminUserDetailsService extends UserDetailsService {

  @Autowired var userDao: UserDao = _
  @Autowired var context: ScalapressContext = _

  def loadUserByUsername(username: String): UserDetails = Option(userDao.byUsername(username)) match {
    case None => throw new UsernameNotFoundException(username)
    case Some(user) => new AdminUserDetails(user, context)
  }
}

class AdminUserDetails(val user: User, context: ScalapressContext) extends ScalaPressUserDetails {

  def isEnabled: Boolean = user.active
  def isCredentialsNonExpired: Boolean = true
  def isAccountNonLocked: Boolean = true
  def isAccountNonExpired: Boolean = true
  def getUsername: String = user.username
  def getPassword: String = user.passwordHash
  def getAuthorities = List(AdminAuthority, UserAuthority).asJava
  def account = {
    context.objectDao.byEmail("admin@localhost") match {
      case None =>

        val accountType = context.accountTypeDao.default

        val account = new Account
        account.name = "admin"
        account.email = "admin@localhost"
        account.accountType = accountType
        context.accountDao.save(account)

        account

      case Some(obj) => account
    }
  }
}

abstract class ScalaPressUserDetails extends UserDetails {
  def account: Account
  def username = getUsername
  def password = getPassword
  def authorities = getAuthorities
  def enabled = isEnabled
}