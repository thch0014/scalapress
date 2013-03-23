package com.liferay.scalapress.obj.tag

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.obj.Obj
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class ObjectSummaryTagTest extends FunSuite with MockitoSugar with BeforeAndAfter {

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]

    test("when max is set then object summary is truncated to the nearest word") {
        val obj = new Obj
        obj.id = 123
        obj
          .content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

        val rendered = SummaryTag.render(ScalapressRequest(req, context).withObject(obj), context, Map("max" -> "20"))
        assert("Lorem ipsum dolor..." === rendered.get)
    }

}