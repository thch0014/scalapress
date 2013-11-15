package com.cloudray.scalapress.service.theme.tag

import org.scalatest.FunSuite
import com.cloudray.scalapress.theme.tag.TagRenderer

/** @author Stephen Samuel */
class TagRendererTest extends FunSuite {

  test("that a param string is parsed") {
    val input = """[attribute?id=21&prefix=<div style="background-color: #20252E;"><strong><font size="2" color="white">Show Dates</font></font></strong><br /></div><br /><div class="disable">&suffix=</div>]"""
    val params = TagRenderer.parseQueryString(input)
    assert(params.size === 3)
  }

}
