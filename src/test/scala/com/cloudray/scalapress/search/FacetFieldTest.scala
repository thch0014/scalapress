package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */
class FacetFieldTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("facet field apply") {
    assert(TagsFacetField === FacetField("tags").get)
    assert(AttributeFacetField(4) === FacetField("attr_facet_4").get)
    assert(None === FacetField("asqew"))
  }

}
