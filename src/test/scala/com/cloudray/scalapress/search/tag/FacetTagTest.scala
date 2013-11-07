package com.cloudray.scalapress.search.tag

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.item.{ItemType, Item}
import com.cloudray.scalapress.theme.Markup
import com.cloudray.scalapress.search.FacetTerm
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.search.Facet
import com.cloudray.scalapress.framework.{ScalapressRequest, ScalapressContext}

/** @author Stephen Samuel */
class FacetTagTest extends FunSuite with MockitoSugar {

  val obj = new Item
  obj.id = 4
  obj.itemType = new ItemType
  obj.itemType.id = 9
  obj.name = "Parachutes"
  obj.status = Item.STATUS_LIVE
  obj.itemType.objectListMarkup = new Markup

  val ref = ItemRef(4, 9, "Parachutes", "Live", Map.empty, Nil)

  val facet = Facet("facety", field = "facety", terms = Seq(FacetTerm("chelsea", 4), FacetTerm("kensington", 2)))
  val r = SearchResult(Seq(ref), Seq(facet))

  val req = mock[HttpServletRequest]
  val context = new ScalapressContext
  val sreq = ScalapressRequest(req, context).withSearchResult(r)

  val tag = new FacetTag

  test("facets decode URL parameters before encoding links") {
    Mockito.when(req.getRequestURL).thenReturn(new StringBuffer("http://cloudray.co.uk/search"))
    Mockito.when(req.getQueryString).thenReturn("sort=Name&attr_8=car_%26_hire")

    val rendered = tag.render(sreq).get

    assert(rendered.contains("attr_8=car_%26_hire"))
    assert(rendered.contains("facety=chelsea"))
    assert(rendered.contains("facety=kensington"))
  }
}
