package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.sksamuel.scoot.soa.{UriBuilder, Paging}

/** @author Stephen Samuel */
class PagingRenderTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val paging = new Paging(UriBuilder("http://www.google.com"), 2, 4)

  test("pagination renderer happy path") {
    val actual = PagingRenderer.render(paging)
    assert(
      """<ul class="pagination"><li class="disabled"><i class="glyphicon glyphicon-search">&nbsp;</i>Viewing results</li><li class=""><a href="http://www.google.com:80?pageNumber=1">1</a></li><li class="active"><a href="http://www.google.com:80?pageNumber=2">2</a></li><li class=""><a href="http://www.google.com:80?pageNumber=3">3</a></li><li class=""><a href="http://www.google.com:80?pageNumber=4">4</a></li></ul>""" === actual)
  }
}
