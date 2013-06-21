package com.cloudray.scalapress.plugin.disqus

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class DisqusSectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    test("disqus render includes shortname") {
        val section = new DisqusSection
        section.shortname = "sammythebull"
        section.render(null).get.contains("sammythebull")
    }
}
