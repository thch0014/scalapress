package com.cloudray.scalapress.security

import org.springframework.security.core.userdetails.{UsernameNotFoundException, UserDetails, UserDetailsService}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import org.springframework.stereotype.Component
import com.cloudray.scalapress.obj.{ObjectDao, Obj}

/** @author Stephen Samuel
  *
  *         Loads user details from the normal objects table
  * */
@Component
class ObjectUserDetailsService extends UserDetailsService {

    @Autowired var objectDao: ObjectDao = _

    def loadUserByUsername(username: String): UserDetails =
        objectDao.byEmail(username) match {
            case None => throw new UsernameNotFoundException(username)
            case Some(obj) => new ObjectUserDetails(obj)
        }
}

class ObjectUserDetails(obj: Obj) extends ScalaPressUserDetails {

    def isEnabled: Boolean = obj.status.toLowerCase == "live"
    def isCredentialsNonExpired: Boolean = true
    def isAccountNonLocked: Boolean = true
    def isAccountNonExpired: Boolean = true
    def getUsername: String = obj.email
    def getPassword: String = obj.passwordHash
    def userObject: Obj = obj
    def getAuthorities = List(UserAuthority).asJava
}