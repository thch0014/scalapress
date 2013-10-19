package com.cloudray.scalapress.security

import org.springframework.security.core.userdetails.{UsernameNotFoundException, UserDetails, UserDetailsService}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import org.springframework.stereotype.Component
import com.cloudray.scalapress.account.{AccountDao, Account}

/** @author Stephen Samuel
  *
  *         Loads user details from the normal objects table
  **/
@Component
@Autowired
class AccountUserDetailsService(accountDao: AccountDao) extends UserDetailsService {

  def loadUserByUsername(username: String): UserDetails =
    accountDao.byEmail(username) match {
      case Some(obj) => new AccountUserDetails(obj)
      case None => throw new UsernameNotFoundException(username)
    }
}

class AccountUserDetails(acc: Account) extends ScalaPressUserDetails {

  def isEnabled: Boolean = acc.isActive
  def isCredentialsNonExpired: Boolean = true
  def isAccountNonLocked: Boolean = acc.isActive
  def isAccountNonExpired: Boolean = true
  def getUsername: String = acc.email
  def getPassword: String = acc.passwordHash
  def account: Account = acc
  def getAuthorities = List(UserAuthority).asJava
}