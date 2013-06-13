package com.cloudray.scalapress.section

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
class SectionTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val context = new ScalapressContext
    context.sectionDao = mock[SectionDao]

    test("default backoffice url is absolute") {
        assert(new Section {
            def render(req: ScalapressRequest): Option[String] = None
            def desc: String = "new section"
        }.backoffice.startsWith("/backoffice"))
    }

    test("new section is visible by default") {
        val section = new Section {
            def render(req: ScalapressRequest): Option[String] = None
            def desc: String = "new section"
        }
        section.init(context)
        assert(section.visible === true)
    }
}
