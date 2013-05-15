package com.liferay.scalapress.plugin.media.smush

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import java.io.ByteArrayInputStream

/** @author Stephen Samuel */
class SmushPluginTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    test("js is minified") {

        val js = "var a = Array();            var b         =Array(); "
        val actual = new SmushPlugin()._minifyJs(new ByteArrayInputStream(js.getBytes("UTF-8")))
        assert("var a=Array();var b=Array();" === new String(actual, "utf-8"))
    }

    test("css is minified") {

        val js = ".class { background-color: #FFFFFF;          text-weight: BOLD;  } "
        val actual = new SmushPlugin()._minifyCss(new ByteArrayInputStream(js.getBytes("UTF-8")))
        assert(".class{background-color:#fff;text-weight:BOLD}" === new String(actual, "utf-8"))
    }
}
