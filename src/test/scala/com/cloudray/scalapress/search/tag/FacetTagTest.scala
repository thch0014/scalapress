package com.cloudray.scalapress.search.tag

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext}
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.obj.{ObjectType, Obj}
import com.cloudray.scalapress.theme.Markup
import com.cloudray.scalapress.search.FacetTerm
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.search.Facet

/** @author Stephen Samuel */
class FacetTagTest extends FunSuite with MockitoSugar {

    val obj = new Obj
    obj.id = 4
    obj.objectType = new ObjectType
    obj.objectType.id = 9
    obj.name = "Parachutes"
    obj.status = Obj.STATUS_LIVE
    obj.objectType.objectListMarkup = new Markup

    val ref = ObjectRef(4, 9, "Parachutes", "Live", Map.empty, Nil)

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
