package com.cloudray.scalapress.widgets

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressRequest

/** @author Stephen Samuel */
class WidgetTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    test("default backoffice url is absolute") {
        assert(new Widget {
            def render(req: ScalapressRequest): Option[String] = None
        }.backoffice.startsWith("/backoffice"))
    }
}
