package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.search.tag.CorpusResultPageIdTag
import com.cloudray.scalapress.search.CorpusResult
import com.cloudray.scalapress.util.Page
import com.cloudray.scalapress.folder.Folder

/** @author Stephen Samuel */
class CorpusResultPageIdTagTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext()
    val req = mock[HttpServletRequest]

    val folder = new Folder
    folder.id = 124
    folder.name = "a team"

    val r = new CorpusResult(Page(folder), "...hannibal loves it when a plan comes together...")

    test("page id uses corpus result") {
        val actual = new CorpusResultPageIdTag().render(ScalapressRequest(req, context).withResult(r))
        assert("124" === actual.get)
    }
}
