package com.cloudray.scalapress.plugin.calendar

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class CalendarWidgetTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    test("backoffice url is absolute") {
        assert(new CalendarWidget().backoffice.startsWith("/backoffice/"))
    }

}
