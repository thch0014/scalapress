package com.cloudray.scalapress.security

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Obj

/** @author Stephen Samuel */
class ObjectUserDetailsService extends FunSuite with MockitoSugar {

    val obj = new Obj
    obj.name = "sammy"
    obj.email = "sam@sammy.com"
    obj.passwordHash = "passhash"

    test("that a user is never expired or locked") {
        assert(new ObjectUserDetails(obj).isAccountNonExpired)
        assert(new ObjectUserDetails(obj).isAccountNonLocked)
        assert(new ObjectUserDetails(obj).isCredentialsNonExpired)
    }

    test("that the password comes from the user obj") {
        assert("passhash" === new ObjectUserDetails(obj).password)
    }

    test("that the username comes from the user obj") {
        assert("sam@sammy.com" === new ObjectUserDetails(obj).username)
    }

    test("that a user is enabled if status is live") {
        obj.status = Obj.STATUS_LIVE
        assert(new ObjectUserDetails(obj).enabled)

        obj.status = Obj.STATUS_DISABLED
        assert(!new ObjectUserDetails(obj).enabled)
    }
}
