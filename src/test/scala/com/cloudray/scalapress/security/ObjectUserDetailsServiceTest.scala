package com.cloudray.scalapress.security

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.account.Account

/** @author Stephen Samuel */
class ObjectUserDetailsServiceTest extends FunSuite with MockitoSugar {

  val obj = new Account
  obj.name = "sammy"
  obj.email = "sam@sammy.com"
  obj.passwordHash = "passhash"

  test("that a user is never expired ") {
    assert(new AccountUserDetails(obj).isAccountNonExpired)
    assert(new AccountUserDetails(obj).isCredentialsNonExpired)
  }

  test("that the password comes from the user obj") {
    assert("passhash" === new AccountUserDetails(obj).password)
  }

  test("that the username comes from the user obj") {
    assert("sam@sammy.com" === new AccountUserDetails(obj).username)
  }

  test("that a user is enabled if status is active") {
    obj.status = Account.STATUS_ACTIVE
    assert(new AccountUserDetails(obj).enabled)

    obj.status = Account.STATUS_ACTIVE
    assert(!new AccountUserDetails(obj).enabled)
  }
}
