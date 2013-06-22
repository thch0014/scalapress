package com.cloudray.scalapress.theme.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import org.joda.time.{DateMidnight, DateTime}
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
class DateCreatedTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val f = new Folder
    f.id = 342
    f.dateCreated = new DateMidnight().getMillis

    val context = new ScalapressContext
    val req = ScalapressRequest(mock[HttpServletRequest], context)

    test("date created is formatted using the format param") {
        assert(new DateTime().getYear.toString + new DateTime().getMonthOfYear
          === new DateCreatedTag().render(req.withFolder(f), Map("format" -> "yyyyM")).get)
    }
}
