package com.cloudray.scalapress.search.controller

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.item.{ItemType, Item, ItemDao}
import com.cloudray.scalapress.theme.{Markup, ThemeService}
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.search.SearchResult
import com.cloudray.scalapress.search.ItemRef

/** @author Stephen Samuel */
class SearchControllerTest extends FunSuite with OneInstancePerTest with MockitoSugar {

  val controller = new SearchController
  controller.searchService = mock[SearchService]
  controller.objectDao = mock[ItemDao]
  controller.themeService = mock[ThemeService]
  controller.searchPluginDao = mock[SearchPluginDao]

  val plugin = new SearchPlugin
  Mockito.when(controller.searchPluginDao.get).thenReturn(plugin)

  val req = mock[HttpServletRequest]
  Mockito.when(req.getRequestURL).thenReturn(new StringBuffer("http://domain.com/hello"))

  val obj = new Item
  obj.id = 4
  obj.itemType = new ItemType
  obj.itemType.id = 9
  obj.name = "Parachutes"
  obj.status = Item.STATUS_LIVE
  obj.itemType.objectListMarkup = new Markup

  val ref = ItemRef(4, 9, "Parachutes", "Live", Map.empty, Nil)

  Mockito.when(controller.objectDao.find(4)).thenReturn(obj)

  test("pagination does not display if total pages is 1") {
    val result = SearchResult(Seq(ref))
    Mockito.when(controller.searchService.search(Matchers.any[Search])).thenReturn(result)
    Mockito.when(controller.objectDao.findBulk(Matchers.any[Seq[Long]])).thenReturn(Seq(obj))

    val page = controller.search(req, null, null, null, "coldplay", null, null, null, 0, null)
    assert(!page.render.contains("pagination"))
  }

  test("pagination is included if number of pages > 1") {

    val result = SearchResult(Seq(ref).padTo(50, ref), count = 200)
    assert(50 === result.refs.size)
    Mockito.when(controller.searchService.search(Matchers.any[Search])).thenReturn(result)
    Mockito.when(controller.objectDao.findBulk(Matchers.any[Seq[Long]])).thenReturn(Seq(obj).padTo(20, obj))

    val page = controller.search(req, null, null, null, "coldplay", null, null, null, 0, null)
    assert(page.render.contains("pagination"))
  }

  test("if object type has no markup then no results are rendered") {
    obj.itemType.objectListMarkup = null
    val result = SearchResult(Seq(ref))
    Mockito.when(controller.searchService.search(Matchers.any[Search])).thenReturn(result)
    Mockito.when(controller.objectDao.findBulk(Matchers.any[Seq[Long]])).thenReturn(Seq(obj))

    val page = controller.search(req, null, null, null, "coldplay", null, null, null, 0, null)
    assert(!page.render.contains("pagination"))
    assert(!page.render.contains("Parachutes"))
  }

  test("if object type has markup then results are rendered") {
    obj.itemType.objectListMarkup.body = "[object]"
    val result = SearchResult(Seq(ref))
    Mockito.when(controller.searchService.search(Matchers.any[Search])).thenReturn(result)
    Mockito.when(controller.objectDao.findBulk(Matchers.any[Seq[Long]])).thenReturn(Seq(obj))

    val page = controller.search(req, null, null, null, "coldplay", null, null, null, 0, null)
    assert(!page.render.contains("pagination"))
    assert(page.render.contains("Parachutes"))
  }

  test("search filters any results that are not live") {
    obj.status = Item.STATUS_DISABLED
    obj.itemType.objectListMarkup.body = "[object]"
    val result = SearchResult(Seq(ref))
    Mockito.when(controller.searchService.search(Matchers.any[Search])).thenReturn(result)
    Mockito.when(controller.objectDao.findBulk(Matchers.any[Seq[Long]])).thenReturn(Seq(obj))

    val page = controller.search(req, null, null, null, "coldplay", null, null, null, 0, null)
    assert(!page.render.contains("Parachutes"))
  }
}
