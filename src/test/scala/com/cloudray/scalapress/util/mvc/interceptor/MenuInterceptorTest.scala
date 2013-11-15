package com.cloudray.scalapress.util.mvc.interceptor

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.framework.ScalapressContext
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

/** @author Stephen Samuel */
class MenuInterceptorTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val context = mock[ScalapressContext]
  val interceptor = new MenuInterceptor(context)
  val mav = new ModelAndView
  val req = mock[HttpServletRequest]
  val resp = mock[HttpServletResponse]

  "a menu interceptor" should "contain multiple items with the same header" in {
    interceptor.postHandle(req, resp, null, mav)
    val menus = mav.getModel.get(MenuInterceptor.KEY_PLUGINMENU).toString
    assert(menus.contains("Delivery Options"))
    assert(menus.contains("Shopping Settings"))
  }

  it should "not render items that are not defined" in {
    interceptor.postHandle(req, resp, null, mav)
    val menus = mav.getModel.get(MenuInterceptor.KEY_PLUGINMENU).toString
    assert(!menus.contains("Simple Pass"))
  }
}
