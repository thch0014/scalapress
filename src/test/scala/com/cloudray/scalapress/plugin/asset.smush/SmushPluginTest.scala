package com.cloudray.scalapress.plugin.asset.smush

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import java.io.ByteArrayInputStream
import org.apache.commons.io.IOUtils

/** @author Stephen Samuel */
class SmushPluginTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("input of type js is minified") {
    val js = "var a = Array();            var b         =Array(); "
    val actual = new SmushPlugin().onStore("some.js", new ByteArrayInputStream(js.getBytes("UTF-8")))
    assert("var a = Array();var b =Array();" === IOUtils.toString(actual._2))
  }

  test("input of type css is minified") {

    val js = ".class { background-color: #FFFFFF;          text-weight: BOLD;  } "
    val actual = new SmushPlugin().onStore("some.css", new ByteArrayInputStream(js.getBytes("UTF-8")))
    assert(".class{background-color:#fff;text-weight:BOLD}" === IOUtils.toString(actual._2))
  }
}
