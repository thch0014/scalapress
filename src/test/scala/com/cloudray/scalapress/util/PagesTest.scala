package com.cloudray.scalapress.util

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class PagesTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  "a pages" should "have a next page when current < total" in {
    val pages = Pages(Page(1, 20), 3)
    assert(pages.hasNext)
  }

  it should "return a next page when current < total" in {
    val pages = Pages(Page(1, 20), 3)
    assert(pages.next == Page(2, 20))
  }

  it should "have a previous page when current > 1" in {
    val pages = Pages(Page(2, 20), 3)
    assert(pages.hasPrevious)
  }

  it should "return a previous page when current > 1" in {
    val pages = Pages(Page(2, 20), 3)
    assert(pages.previous == Page(1, 20))
  }

  it should "be first when page is 1" in {
    assert(Pages(Page(1, 20), 2).isFirst)
    assert(!Pages(Page(2, 20), 2).isFirst)
  }

  it should "be last when current page == total pages" in {
    assert(Pages(Page(7, 20), 7).isLast)
    assert(!Pages(Page(6, 20), 7).isLast)
  }

  it should "be bounded by 1 when returning before range" in {
    assert(Pages(Page(5, 20), 7).before(100).start === 1)
  }

  it should "respect k when returning before range" in {
    assert(Pages(Page(5, 20), 7).before(2).start === 3)
  }

  it should "be bounded by last page when returning after range" in {
    assert(Pages(Page(5, 20), 7).after(100).end === 7)
  }

  it should "respect k when returning after range" in {
    assert(Pages(Page(5, 20), 14).after(2).end === 7)
  }

  it should "return true for multiple when total pages > 1" in {
    assert(Pages(Page(5, 20), 2).multiple)
    assert(!Pages(Page(5, 20), 1).multiple)
  }
}
