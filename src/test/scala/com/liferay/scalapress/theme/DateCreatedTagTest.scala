package com.liferay.scalapress.theme

import org.scalatest.mock.MockitoSugar
import org.scalatest.{FunSuite, BeforeAndAfter}
import tag.DateCreatedTag
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
class DateCreatedTagTest extends FunSuite with BeforeAndAfter with MockitoSugar {

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]

    val obj = new Obj
    obj.dateCreated = 1364122808957l

    val f = new Folder
    f.dateCreated = 1364122808957l


    test("custom format object happy path") {

        val sreq = new ScalapressRequest(req, context).withObject(obj)
        val rendered = new DateCreatedTag().render(sreq, context, Map("format" -> "yyyy!MM!dd"))
        assert("2013!03!24" === rendered.get)
    }

    test("custom format folder happy path") {
        val sreq = new ScalapressRequest(req, context).withFolder(f)
        val rendered = new DateCreatedTag().render(sreq, context, Map("format" -> "yyyy^MM^dd"))
        assert("2013^03^24" === rendered.get)
    }
}