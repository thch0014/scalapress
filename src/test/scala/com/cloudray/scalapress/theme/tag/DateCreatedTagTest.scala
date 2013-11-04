package com.cloudray.scalapress.theme.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import org.joda.time.{DateTimeZone, DateMidnight, DateTime}
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class DateCreatedTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val f = new Folder
    f.id = 342
    f.dateCreated = new DateMidnight(DateTimeZone.UTC).getMillis

    val context = new ScalapressContext
    val req = ScalapressRequest(mock[HttpServletRequest], context)

    test("date created is formatted using the format param") {
        assert(new DateTime(DateTimeZone.UTC).getYear.toString + new DateTime(DateTimeZone.UTC).getMonthOfYear
          === new DateCreatedTag().render(req.withFolder(f), Map("format" -> "yyyyM")).get)
    }
}
