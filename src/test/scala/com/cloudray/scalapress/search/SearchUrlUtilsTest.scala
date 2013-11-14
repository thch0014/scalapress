package com.cloudray.scalapress.search

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito

/** @author Stephen Samuel */
class SearchUrlUtilsTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val req = mock[HttpServletRequest]
  Mockito.when(req.getRequestURL).thenReturn(new StringBuffer("http://www.coldplay.com"))
  Mockito.when(req.getQueryString).thenReturn("afacet_12=spaghetti&afacet_15=tomato&sort=Newest")

  "a search url parser" should "parse selected facets from parameters" in {
    val selected = SearchUrlUtils.facets(req)
    assert(2 === selected.size)
    assert(12 === selected(0).field.asInstanceOf[AttributeFacetField].id)
    assert("spaghetti" === selected(0).value)
    assert(15 === selected(1).field.asInstanceOf[AttributeFacetField].id)
    assert("tomato" === selected(1).value)
  }

  it should "parse sort from parameters" in {
    val sort = SearchUrlUtils.sort(req)
    assert(Sort.Newest === sort)
  }

  it should "return default Sort for invalid sort parameter" in {
    Mockito.when(req.getQueryString).thenReturn("afacet_12=spaghetti&afacet_15=tomato&sort=weqew")
    val sort = SearchUrlUtils.sort(req)
    assert(Sort.Name === sort)
  }

  it should "return default Sort for missing sort parameter" in {
    Mockito.when(req.getQueryString).thenReturn("afacet_12=spaghetti&afacet_15=tomato")
    val sort = SearchUrlUtils.sort(req)
    assert(Sort.Name === sort)
  }
}
