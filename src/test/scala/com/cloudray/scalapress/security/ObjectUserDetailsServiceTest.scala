package com.cloudray.scalapress.security

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.account.Account

/** @author Stephen Samuel */
class ObjectUserDetailsServiceTest extends FunSuite with MockitoSugar {

  val acc = new Account
  acc.name = "sammy"
  acc.email = "sam@sammy.com"
  acc.passwordHash = "passhash"

  test("that a user is never expired ") {
    assert(new AccountUserDetails(acc).isAccountNonExpired)
    assert(new AccountUserDetails(acc).isCredentialsNonExpired)
  }

  test("that the password comes from the user obj") {
    assert("passhash" === new AccountUserDetails(acc).password)
  }

  test("that the username comes from the user obj") {
    assert("sam@sammy.com" === new AccountUserDetails(acc).username)
  }

  test("that a user is enabled if status is active") {
    acc.status = Account.STATUS_ACTIVE
    assert(new AccountUserDetails(acc).enabled)

    acc.status = Account.STATUS_DISABLED
    assert(!new AccountUserDetails(acc).enabled)
  }
}
