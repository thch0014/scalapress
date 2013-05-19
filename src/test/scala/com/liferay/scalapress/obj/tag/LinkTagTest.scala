package com.liferay.scalapress.obj.tag

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
class LinkTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext()
    val req = mock[HttpServletRequest]

    val obj = new Obj
    obj.id = 12
    obj.name = "meatballs"

    val f = new Folder
    f.id = 435
    f.name = "italian foods"

    test("obj is used if set") {
        val sreq = ScalapressRequest(req, context).withObject(obj)
        val actual = LinkTag.render(sreq, Map.empty).get
        assert("/object-12-meatballs" === actual)
    }

    test("folder is used if set") {
        val sreq = ScalapressRequest(req, context).withFolder(f)
        val actual = LinkTag.render(sreq, Map.empty).get
        assert("/folder-435-italian-foods" === actual)
    }
}
