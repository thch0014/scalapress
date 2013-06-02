package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.search.tag.CorpusResultTitleTag
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.util.UrlGenerator

/** @author Stephen Samuel */
class CorpusResultPageNameTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext()
    val req = mock[HttpServletRequest]

    val folder = new Folder
    folder.id = 124
    folder.name = "a team"

    val r = new CorpusResult(folder.name, UrlGenerator.url(folder), "...hannibal loves it when a plan comes together...")

    test("page name uses corpus result") {
        val actual = new CorpusResultTitleTag().render(ScalapressRequest(req, context).withResult(r))
        assert("a team" === actual.get)
    }
}
