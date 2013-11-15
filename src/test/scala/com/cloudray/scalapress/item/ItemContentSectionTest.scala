package com.cloudray.scalapress.item

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class ItemContentSectionTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val section = new ItemContentSection
  section.content = "my super content"

  "an item content section" should "render the sections content" in {
    assert("my super content" === section.render(null).get)
  }

  it should "render none if content is null" in {
    section.content = null
    assert(section.render(null).isEmpty)
  }
}
