package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.search.tag.CorpusResultUrlTag
import com.cloudray.scalapress.util.UrlGenerator
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class CorpusResultUrlTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext()
    val req = mock[HttpServletRequest]

    val folder = new Folder
    folder.id = 124
    folder.name = "a team"

    val r = new CorpusResult(folder.name, UrlGenerator.url(folder), "...hannibal loves it when a plan comes together...")

    test("corpus result url tag uses url from corpus result page") {
        val actual = new CorpusResultUrlTag().render(ScalapressRequest(req, context).withResult(r))
        assert(UrlGenerator.url(folder) === actual.get)
    }
}
