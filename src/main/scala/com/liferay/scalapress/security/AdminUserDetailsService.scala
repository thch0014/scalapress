package com.liferay.scalapress.security

import org.springframework.security.core.userdetails.{UsernameNotFoundException, UserDetails, UserDetailsService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.user.{User, UserDao}

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
    def userObject: Obj = {
        context.objectDao.byEmail("admin@localhost") match {
            case None =>

                val accountType = context.typeDao.findAll()
                  .find(t => t.name.toLowerCase == "account" || t.name.toLowerCase == "accounts").get

                val obj = new Obj
                obj.name = "admin"
                obj.email = "admin@localhost"
                obj.objectType = accountType
                context.objectDao.save(obj)

                obj

            case Some(obj) => obj
        }

    }
}

abstract class ScalaPressUserDetails extends UserDetails {
    // returns the user object for the current security context
    def userObject: Obj
    def username = getUsername
    def password = getPassword
    def authorities = getAuthorities
    def enabled = isEnabled
}