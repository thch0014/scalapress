package com.cloudray.scalapress.plugin.search.facet

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.search.{AttributeFacetField, FacetField, TagsFacetField}

/** @author Stephen Samuel */
class FacetFieldTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  test("facet field apply") {
    assert(TagsFacetField === FacetField("tags").get)
    assert(AttributeFacetField(4) === FacetField("afacet_4").get)
    assert(None === FacetField("asqew"))
  }

}
