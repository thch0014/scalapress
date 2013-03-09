package com.liferay.scalapress.service.security

import org.springframework.security.core.userdetails.{UsernameNotFoundException, UserDetails, UserDetailsService}
import com.liferay.scalapress.dao.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.domain.User
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Component
class AdminUserDetailsService extends UserDetailsService {

    @Autowired var userDao: UserDao = _

    def loadUserByUsername(username: String): UserDetails = Option(userDao.byUsername(username)) match {
        case None => throw new UsernameNotFoundException(username)
        case Some(user) => new BasicUserDetails(user)
    }
}

class BasicUserDetails(val user: User) extends UserDetails {

    def isEnabled: Boolean = user.active
    def isCredentialsNonExpired: Boolean = true
    def isAccountNonLocked: Boolean = true
    def isAccountNonExpired: Boolean = true
    def getUsername: String = user.username
    def getPassword: String = user.passwordHash
    def getAuthorities = List(AdminAuthority, UserAuthority).asJava
}