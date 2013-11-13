package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.search.tag.CorpusResultSnippetTag
import com.cloudray.scalapress.framework.{UrlGenerator, ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class CorpusResultSnippetTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext()
    val req = mock[HttpServletRequest]

    val folder = new Folder
    folder.id = 124
    folder.name = "a team"

    val r = new CorpusResult(folder.name, UrlGenerator.url(folder), "...hannibal loves it when a plan comes together...")

    test("corpus result snippet tag uses snippet from corpus result") {
        val actual = new CorpusResultSnippetTag().render(ScalapressRequest(req, context).withResult(r))
        assert("...hannibal loves it when a plan comes together..." === actual.get)
    }
}
