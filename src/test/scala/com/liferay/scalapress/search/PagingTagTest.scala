package com.liferay.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.sksamuel.scoot.soa.{UriBuilder, Paging}

/** @author Stephen Samuel */
class PagingTagTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val paging = new Paging(UriBuilder("http://www.google.com"), 4, 13)

    test("pagination tag uses range param") {
        val actual = PagingRenderer.render(paging)
        assert(
            "<div class=\"pagination\"><ul><li><a href=\"http://www.google.com:80?pageNumber=2\">2</a></li><li><a href=\"http://www.google.com:80?pageNumber=3\">3</a></li><li><a href=\"http://www.google.com:80?pageNumber=4\">4</a></li><li><a href=\"http://www.google.com:80?pageNumber=5\">5</a></li><li><a href=\"http://www.google.com:80?pageNumber=6\">6</a></li></ul></div>" === actual)
    }
}
