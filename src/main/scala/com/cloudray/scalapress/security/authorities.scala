package com.cloudray.scalapress.security

import org.springframework.security.core.GrantedAuthority

/** @author Stephen Samuel */

object UserAuthority extends GrantedAuthority {
    def getAuthority: String = "ROLE_USER"
}

object AdminAuthority extends GrantedAuthority {
    def getAuthority: String = "ROLE_ADMIN"
}