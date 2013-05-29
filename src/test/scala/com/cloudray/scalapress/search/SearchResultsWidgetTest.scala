package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.obj.{ObjectDao, Obj}
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.search.widget.SearchResultsWidget

/** @author Stephen Samuel */
class SearchResultsWidgetTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val section = new SearchResultsWidget
    val req = mock[HttpServletRequest]
    val context = new ScalapressContext
    context.searchService = mock[SearchService]
    context.objectDao = mock[ObjectDao]

    val obj1 = new Obj
    obj1.name = "michael"
    obj1.status = Obj.STATUS_LIVE
    obj1.id = 1

    val obj2 = new Obj
    obj2.name = "april"
    obj2.status = Obj.STATUS_LIVE
    obj2.id = 2

    val obj3 = new Obj
    obj3.name = "devon"
    obj3.status = Obj.STATUS_LIVE
    obj3.id = 3

    val ref1 = ObjectRef(obj1.id, 55, obj1.name, Obj.STATUS_LIVE, Map.empty, Nil)
    val ref2 = ObjectRef(obj2.id, 55, obj2.name, Obj.STATUS_LIVE, Map.empty, Nil)
    val ref3 = ObjectRef(obj3.id, 55, obj3.name, Obj.STATUS_LIVE, Map.empty, Nil)

    val sreq = ScalapressRequest(req, context)
    section.search = new SavedSearch

    test("that the bulk loaded objects should use the same ordering as the search results") {

        val sortedObjs = Seq(obj1, obj2, obj3)
        val sortedRefs = Seq(ref2, ref3, ref1)
        val result = SearchResult(sortedRefs)

        // search returns sorted refs
        Mockito.when(context.searchService.search(section.search)).thenReturn(result)
        // bulk dao method returns them in id order
        Mockito.when(context.objectDao.findBulk(Matchers.any[Seq[Long]])).thenReturn(sortedObjs)

        // test should be that they are re-ordered back to the sorted refs order
        val expected = Seq(obj2, obj3, obj1)
        val actual = section._objects(sreq)
        assert(expected === actual)
    }
}
