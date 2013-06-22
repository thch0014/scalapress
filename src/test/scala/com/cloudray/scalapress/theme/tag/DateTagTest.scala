package com.cloudray.scalapress.theme.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import org.joda.time.DateTime

/** @author Stephen Samuel */
class DateTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext
    val req = ScalapressRequest(mock[HttpServletRequest], context)

    test("date is formatted using the format param") {
        assert(new DateTime().getYear.toString === new DateTag().render(req, Map("format" -> "yyyy")).get)
        assert(new DateTime().getMonthOfYear.toString === new DateTag().render(req, Map("format" -> "M")).get)
    }
}
