package com.cloudray.scalapress.util

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class PagedResultTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  "a paged result" should "use results for size" in {
    val results = List("a", "b", "c")
    val result = PagedResult(results)
    assert(result.page.pageNumber === 1)
    assert(result.page.pageSize === 3)
    assert(result.totalResults === 3)
  }

  it should "use defaults for empty" in {
    val result = PagedResult.empty
    assert(result.page.pageNumber === 1)
    assert(result.page.pageSize === 20)
    assert(result.totalResults === 0)
  }
}
