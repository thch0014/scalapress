package com.cloudray.scalapress.util

import org.scalatest.{OneInstancePerTest, FlatSpec}
import org.scalatest.mock.MockitoSugar
import com.github.theon.uri.Uri

/** @author Stephen Samuel */
class PageUrlUtilsTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  "a helper" should "replace pageSize and pageNumber with given values" in {
    val uri = Uri.parseUri("http://domain.com?pageSize=14&pageNumber=5")
    val uri2 = PageUrlUtils.append(uri, Page(3, 50))
    assert(uri2.query.params("pageSize").head === "50")
    assert(uri2.query.params("pageNumber").head === "3")
  }

}
