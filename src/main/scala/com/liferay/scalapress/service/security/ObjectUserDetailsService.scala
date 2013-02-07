package com.liferay.scalapress.service.security

import org.springframework.security.core.userdetails.{UsernameNotFoundException, UserDetails, UserDetailsService}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.ObjectDao
import com.liferay.scalapress.domain.Obj
import java.util
import org.springframework.security.core.GrantedAuthority
import scala.collection.JavaConverters._
import org.springframework.stereotype.Component

/** @author Stephen Samuel */
@Component
class ObjectUserDetailsService extends UserDetailsService {

    @Autowired var objectDao: ObjectDao = _

    def loadUserByUsername(username: String): UserDetails = objectDao.byEmail(username) match {
        case None => throw new UsernameNotFoundException(username)
        case Some(obj) => new ObjectUserDetails(obj)
    }
}

class ObjectUserDetails(obj: Obj) extends UserDetails {

    def isEnabled: Boolean = obj.status.toLowerCase == "live"
    def isCredentialsNonExpired: Boolean = true
    def isAccountNonLocked: Boolean = true
    def isAccountNonExpired: Boolean = true
    def getUsername: String = obj.email
    def getPassword: String = obj.passwordHash
    def userId: Long = obj.id
    def getAuthorities: util.Collection[_ <: GrantedAuthority] = List(UserAuthority).asJava
}

object UserAuthority extends GrantedAuthority {
    def getAuthority: String = "ROLE_USER"
}