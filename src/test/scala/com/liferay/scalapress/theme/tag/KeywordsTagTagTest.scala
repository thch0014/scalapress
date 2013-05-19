package com.liferay.scalapress.theme.tag

import org.scalatest.mock.MockitoSugar
import org.scalatest.{OneInstancePerTest, FunSuite}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.folder.Folder

/** @author Stephen Samuel */
class KeywordsTagTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val req = mock[HttpServletRequest]
    val context = mock[ScalapressContext]

    val obj = new Obj
    obj.dateCreated = 1364122808957l
    obj.keywordsTag = "I love keywords me"

    val f = new Folder
    f.dateCreated = 1364122808957l
    f.keywordsTag = "folders have the best keywords"


    test("keywords tag uses desc field of object") {
        val sreq = new ScalapressRequest(req, context).withObject(obj)
        val rendered = new KeywordsTagTag().render(sreq, Map.empty)
        assert("<meta name='keywords' content='I love keywords me'/>" === rendered.get)
    }

    test("keywords tag uses desc field of folder") {
        val sreq = new ScalapressRequest(req, context).withFolder(f)
        val rendered = new KeywordsTagTag().render(sreq, Map.empty)
        assert("<meta name='keywords' content='folders have the best keywords'/>" === rendered.get)
    }
}
