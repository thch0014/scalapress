package com.cloudray.scalapress

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.mockito.Mockito

/** @author Stephen Samuel */
class VaryEncodingInterceptorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  test("vary encoding interceptor") {
    VaryEncodingInterceptor.afterCompletion(req, resp, null, null)
    Mockito.verify(resp).setHeader("Vary", "Accept-Encoding")
  }
}
