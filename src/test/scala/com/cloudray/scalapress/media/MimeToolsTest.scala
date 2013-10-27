package com.cloudray.scalapress.media

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class MimeToolsTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  "mime tools" should "return text/css for filenames that end in css" in {
    assert("text/css" === MimeTools.contentType("filename.css"))
  }

  "mime tools" should "return image/png for filenames that end in png" in {
    assert("image/png" === MimeTools.contentType("filename.png"))
  }
}
