package com.cloudray.scalapress.plugin.sqlcorpussearch

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.search.CorpusResult
import com.cloudray.scalapress.util.Page
import com.cloudray.scalapress.folder.Folder
import com.cloudray.scalapress.search.tag.CorpusResultUrlTag

/** @author Stephen Samuel */
class CorpusResultUrlTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val context = new ScalapressContext()
    val req = mock[HttpServletRequest]

    val folder = new Folder
    folder.id = 124
    folder.name = "a team"

    val r = new CorpusResult(Page(folder), "...hannibal loves it when a plan comes together...")

    test("corpus result url tag uses url from corpus result page") {
        val actual = new CorpusResultUrlTag().render(ScalapressRequest(req, context).withResult(r))
        assert("/object-124-a-team" === actual.get)
    }
}
