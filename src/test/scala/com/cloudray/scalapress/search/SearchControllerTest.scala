package com.cloudray.scalapress.search

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.search.controller.SearchController
import javax.servlet.http.HttpServletRequest
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.obj.{ObjectType, Obj, ObjectDao}
import com.cloudray.scalapress.theme.{Markup, ThemeService}

/** @author Stephen Samuel */
class SearchControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val controller = new SearchController
    controller.searchService = mock[SearchService]
    controller.objectDao = mock[ObjectDao]
    controller.themeService = mock[ThemeService]
    controller.searchPluginDao = mock[SearchPluginDao]

    val plugin = new SearchPlugin
    Mockito.when(controller.searchPluginDao.get).thenReturn(plugin)

    val req = mock[HttpServletRequest]
    Mockito.when(req.getRequestURL).thenReturn(new StringBuffer("http://domain.com/hello"))

    val obj = new Obj
    obj.id = 4
    obj.objectType = new ObjectType
    obj.objectType.id = 9
    obj.name = "Parachutes"
    obj.status = Obj.STATUS_LIVE
    obj.objectType.objectListMarkup = new Markup

    val ref = ObjectRef(4, 9, "Parachutes", "Live", Map.empty, Nil)

    Mockito.when(controller.objectDao.find(4)).thenReturn(obj)

    test("pagination does not display if total pages is 1") {
        val result = SearchResult(Seq(ref))
        Mockito.when(controller.searchService.search(Matchers.any[SavedSearch])).thenReturn(result)

        val page = controller.search(req, 0, null, "coldplay", null, null, null, 0, null)
        assert(!page.render.contains("pagination"))
    }

    test("pagination is included if number of pages > 1") {

        val result = SearchResult(Seq(ref).padTo(50, ref))
        assert(50 === result.refs.size)
        Mockito.when(controller.searchService.search(Matchers.any[SavedSearch])).thenReturn(result)

        val page = controller.search(req, 0, null, null, "coldplay", null, null, 0, null)
        assert(page.render.contains("pagination"))
    }

    test("if object type has no markup then no results are rendered") {
        obj.objectType.objectListMarkup = null
        val result = SearchResult(Seq(ref))
        Mockito.when(controller.searchService.search(Matchers.any[SavedSearch])).thenReturn(result)

        val page = controller.search(req, 0, null, null, "coldplay", null, null, 0, null)
        assert(!page.render.contains("pagination"))
        assert(!page.render.contains("Parachutes"))
    }

    test("if object type has markup then results are rendered") {
        obj.objectType.objectListMarkup.body = "[object]"
        val result = SearchResult(Seq(ref))
        Mockito.when(controller.searchService.search(Matchers.any[SavedSearch])).thenReturn(result)

        val page = controller.search(req, 0, null, "coldplay", null, null, null, 0, null)
        assert(!page.render.contains("pagination"))
        assert(page.render.contains("Parachutes"))
    }

    test("if id is specified then search loads obj directly") {
        controller.search(req, 234, null, null, "coldplay", null, null, 0, null)
        Mockito.verify(controller.objectDao).find(234)
    }

    test("search filters any results that are not live") {
        obj.status = Obj.STATUS_DISABLED
        obj.objectType.objectListMarkup.body = "[object]"
        val result = SearchResult(Seq(ref))
        Mockito.when(controller.searchService.search(Matchers.any[SavedSearch])).thenReturn(result)

        val page = controller.search(req, 0, null, null, "coldplay", null, null, 0, null)
        assert(!page.render.contains("Parachutes"))
    }
}
