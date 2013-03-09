package com.liferay.scalapress.service.security

import org.springframework.security.core.userdetails.{UsernameNotFoundException, UserDetails, UserDetailsService}
import com.liferay.scalapress.dao.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util
import org.springframework.security.core.GrantedAuthority
import com.liferay.scalapress.domain.User

/** @author Stephen Samuel */
@Component
class DatabaseUserDetailsService extends UserDetailsService {

    @Autowired var userDao: UserDao = _

    def loadUserByUsername(username: String): UserDetails = Option(userDao.byUsername(username)) match {
        case None => throw new UsernameNotFoundException(username)
        case Some(user) => new BasicUserDetails(user)
    }
}

class BasicUserDetails(user: User) extends UserDetails {

    def isEnabled: Boolean = user.active
    def isCredentialsNonExpired: Boolean = user.active
    def isAccountNonLocked: Boolean = user.active
    def isAccountNonExpired: Boolean = user.active
    def getUsername: String = user.username
    def getPassword: String = user.passwordHash

    import scala.collection.JavaConverters._

    def getAuthorities: util.Collection[_ <: GrantedAuthority] = List(AdminAuthority).asJava
}

object AdminAuthority extends GrantedAuthority {
    def getAuthority: String = "ROLE_ADMIN"
}