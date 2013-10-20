package com.cloudray.scalapress.user

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.mockito.{Matchers, Mockito}

/** @author Stephen Samuel */
class UserEditControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val userDao = mock[UserDao]
  val passwordEncoder = mock[PasswordEncoder]
  val controller = new UserEditController(userDao, passwordEncoder)
  Mockito.when(passwordEncoder.encodePassword(Matchers.anyString, Matchers.anyString)).thenReturn("encrypted")

  val user = new User
  user.name = "sam"
  user.username = "sammy1"

  test("only update password if changePassword is set") {
    user.passwordHash = "12345"
    controller.save(user)
    assert("12345" === user.passwordHash)
  }

  test("if changePassword is set then password is hashed and set") {
    user.passwordHash = "12345"
    user.changePassword = "newpass"
    controller.save(user)
    assert("encrypted" === user.passwordHash)
  }
}
