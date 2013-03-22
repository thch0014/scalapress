package com.liferay.scalapress.security

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.user.User
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
class AdminUserDetailsServiceTest extends FunSuite with MockitoSugar {

    val user = new User
    user.name = "sammy"
    val context = mock[ScalapressContext]

    test("that an admin user is never expired or locked") {
        assert(new AdminUserDetails(user, context).isAccountNonExpired)
        assert(new AdminUserDetails(user, context).isAccountNonLocked)
        assert(new AdminUserDetails(user, context).isAccountNonLocked)
    }

    test("that an admin user is only enabled if active") {
        user.active = true
        assert(new AdminUserDetails(user, context).isEnabled)
        user.active = false
        assert(!new AdminUserDetails(user, context).isEnabled)
    }
}
