package com.cloudray.scalapress.util

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.util.mvc.HomepageForwardController

/** @author Stephen Samuel */
class HomepageForwardControllerTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val controller = new HomepageForwardController

  "a homepage forward controller" should "use root folder as default forward" in {
    val url = controller.homepage
    assert("forward:/folder" === url)
  }
}
