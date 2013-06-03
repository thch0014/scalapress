package com.cloudray.scalapress.section

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressRequest

/** @author Stephen Samuel */
class SectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    test("default backoffice url is absolute") {
        assert(new Section {
            def render(req: ScalapressRequest): Option[String] = None
            def desc: String = "new section"
        }.backoffice.startsWith("/backoffice"))
    }
}
