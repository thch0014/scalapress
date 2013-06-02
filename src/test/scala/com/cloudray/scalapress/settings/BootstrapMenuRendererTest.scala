package com.cloudray.scalapress.settings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class BootstrapMenuRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    test("all menu types are rendered") {

        val items = Seq(
            MenuLink("a", None, "coldplay.com"),
            MenuDivider,
            Menu("b", None, Seq(
                MenuLink("c", None, "keane.com"),
                MenuLink("d", None, "jethrotull.com"))))

        val actual = BootstrapMenuRenderer.render(items)
        assert(
            "<ul class=\"dropdown-menu\" role=\"menu\"><li><a tabindex=\"-1\" href=\"coldplay.com\">a</a></li><li class=\"divider\"/><li class=\"dropdown-submenu\"><a tabindex=\"-1\" href=\"#\">b</a><ul class=\"dropdown-menu\" role=\"menu\"><li><a tabindex=\"-1\" href=\"keane.com\">c</a></li><li><a tabindex=\"-1\" href=\"jethrotull.com\">d</a></li></ul></li></ul>" === actual
              .toString())
    }
}
