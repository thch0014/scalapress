package com.liferay.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.search.tag.PagingTag
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.sksamuel.scoot.soa.{UriBuilder, Paging}

/** @author Stephen Samuel */
class PagingTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val req = mock[HttpServletRequest]
    val context = new ScalapressContext()
    val paging = new Paging(UriBuilder("http://www.google.com"), 4, 13)
    val sreq = ScalapressRequest(req, context).withPaging(paging)

    test("pagination tag uses default range of 5 pages either side") {
        val actual = new PagingTag().render(sreq.withPaging(paging), context, Map.empty)
        assert(
            "<div class=\"pagination\"><ul><li><a href=\"http://www.google.com:80?pageNumber=1\">1</a></li><li><a href=\"http://www.google.com:80?pageNumber=2\">2</a></li><li><a href=\"http://www.google.com:80?pageNumber=3\">3</a></li><li><a href=\"http://www.google.com:80?pageNumber=4\">4</a></li><li><a href=\"http://www.google.com:80?pageNumber=5\">5</a></li><li><a href=\"http://www.google.com:80?pageNumber=6\">6</a></li><li><a href=\"http://www.google.com:80?pageNumber=7\">7</a></li><li><a href=\"http://www.google.com:80?pageNumber=8\">8</a></li><li><a href=\"http://www.google.com:80?pageNumber=9\">9</a></li></ul></div>" === actual
              .get)
    }

    test("pagination tag uses range param") {
        val actual = new PagingTag().render(sreq.withPaging(paging), context, Map("range" -> "2"))
        assert(
            "<div class=\"pagination\"><ul><li><a href=\"http://www.google.com:80?pageNumber=2\">2</a></li><li><a href=\"http://www.google.com:80?pageNumber=3\">3</a></li><li><a href=\"http://www.google.com:80?pageNumber=4\">4</a></li><li><a href=\"http://www.google.com:80?pageNumber=5\">5</a></li><li><a href=\"http://www.google.com:80?pageNumber=6\">6</a></li></ul></div>" === actual
              .get)
    }
}
