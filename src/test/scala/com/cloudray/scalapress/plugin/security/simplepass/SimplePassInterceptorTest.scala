package com.cloudray.scalapress.plugin.security.simplepass

import org.scalatest.{FunSuite, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import com.cloudray.scalapress.folder.Folder
import org.mockito.Mockito
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class SimplePassInterceptorTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val dao = mock[SimplePassPluginDao]
  val interceptor = new SimplePassInterceptor(dao)

  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  val restricted1 = new Folder
  restricted1.id = 14
  val restricted2 = new Folder
  restricted2.id = 19

  val restricted = Set(restricted1, restricted2)

  val plugin = new SimplePassPlugin
  plugin.folders = restricted.asJava
  plugin.username = "admin"
  plugin.password = "letmein"

  Mockito.when(dao.get).thenReturn(plugin)

  test("the interceptor should deny a restricted folder with no auth details") {
    val folder = new Folder
    folder.id = 14
    assert(!interceptor.preHandle(folder, req, resp))
  }

  test("the interceptor should accept a restricted folder with valid auth details") {
    val folder = new Folder
    folder.id = 14
    Mockito.when(req.getHeader("Authorization")).thenReturn("Basic YWRtaW46bGV0bWVpbg==")
    assert(interceptor.preHandle(folder, req, resp))
  }

  test("the interceptor should accept a public folder with no auth details") {
    val folder = new Folder
    folder.id = 33
    assert(interceptor.preHandle(folder, req, resp))
  }
}
