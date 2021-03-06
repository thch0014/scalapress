package com.cloudray.scalapress.account.controller.tag

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.security.SecurityResolver
import org.mockito.Mockito
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class UsernameTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val tag = new UsernameTag
  tag.securityResolver = mock[SecurityResolver]
  val req = mock[HttpServletRequest]
  val user = new Account
  user.name = "sammy"

  val sreq = ScalapressRequest(req, new ScalapressContext)

  "a username tag" should "render with a prefix when set" in {
    Mockito.when(tag.securityResolver.getAccount(req)).thenReturn(Some(user))
    val actual = tag.render(sreq, Map("prefix" -> "Welcome back "))
    assert("Welcome back sammy" === actual.get)
  }

  "a username tag" should "render None when no user is set" in {
    Mockito.when(tag.securityResolver.getAccount(req)).thenReturn(None)
    val actual = tag.render(sreq)
    assert(actual.isEmpty)
  }
}
