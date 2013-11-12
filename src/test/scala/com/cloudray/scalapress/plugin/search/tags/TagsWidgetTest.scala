package com.cloudray.scalapress.plugin.search.tags

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class TagsWidgetTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  test("render tags adds spans") {
    val tags = Seq("eastwood", "morricone")
    val actual = new TagsWidget()._renderTags(tags)
    assert(Seq(<span>eastwood</span>, <span>morricone</span>) === actual)
  }
}
