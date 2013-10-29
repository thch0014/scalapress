package com.cloudray.scalapress.account.controller.tag

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.security.SecurityResolver
import org.mockito.Mockito
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class AccountLinkTagTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val tag = new AccountLinkTag
  tag.securityResolver = mock[SecurityResolver]
  val req = mock[HttpServletRequest]
  val user = new Item
  user.name = "sammy"

  val sreq = ScalapressRequest(req, new ScalapressContext)

  "an account link tag" should "render login link when no user is set" in {
    Mockito.when(tag.securityResolver.hasUserRole(req)).thenReturn(false)
    val actual = tag.render(sreq)
    assert("<a href='/login' class='' id='' rel=''>Login or Register</a>" === actual.get)
  }

  it should "render account link when user is present" in {
    Mockito.when(tag.securityResolver.hasUserRole(req)).thenReturn(true)
    val actual = tag.render(sreq)
    assert("<a href='/account' class='' id='' rel=''>Account</a>" === actual.get)
  }
}
