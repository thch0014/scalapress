package com.cloudray.scalapress.settings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class BootstrapMenuRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("all menu types are rendered") {

    val items = Seq(
      MenuLink("a", None, "coldplay.com"),
      MenuDivider,
      MenuHeader("b")
    )

    val actual = BootstrapMenuRenderer.render(items)
    assert("" === actual
      .toString())
  }
}
