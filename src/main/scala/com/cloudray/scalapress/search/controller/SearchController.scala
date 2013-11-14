package com.cloudray.scalapress.search.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.search.section.SearchFormSection
import com.cloudray.scalapress.item.{Item, ItemDao, TypeDao}
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.theme.{ThemeService, MarkupRenderer}
import com.sksamuel.scoot.soa.{Paging, Page}
import com.cloudray.scalapress.widgets.WidgetDao
import com.cloudray.scalapress.search.widget.SearchFormWidget
import com.cloudray.scalapress.framework.{Logging, ScalapressRequest, ScalapressContext}
import com.cloudray.scalapress.item.attr.AttributeSelection
import com.cloudray.scalapress.util.PageUrlUtils

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("search"))
class SearchController extends Logging {

  @Autowired var savedSearchDao: SavedSearchDao = _
  @Autowired var searchService: SearchService = _
  @Autowired var objectDao: ItemDao = _
  @Autowired var typeDao: TypeDao = _
  @Autowired var themeService: ThemeService = _
  @Autowired var context: ScalapressContext = _
  @Autowired var searchPluginDao: SearchPluginDao = _
  @Autowired var sectionDao: SectionDao = _
  @Autowired var widgetDao: WidgetDao = _

  @ResponseBody
  @RequestMapping(value = Array("count"), produces = Array("text/html"))
  def count = searchService.count.toString

  def _isAttributeParameter(param: String): Boolean =
    try {
      param.drop("attr_".length).toLong
      true
    } catch {
      case _: Throwable => false
    }

  @ResponseBody
  @RequestMapping(produces = Array("text/html"))
  def search(req: HttpServletRequest,
             @RequestParam(value = "sort", required = false) sort: Sort,
             @RequestParam(value = "sectionId", required = false) sectionId: String,
             @RequestParam(value = "widgetId", required = false) widgetId: String,
             @RequestParam(value = "q", required = false) q: String,
             @RequestParam(value = "name", required = false) name: String,
             @RequestParam(value = "type", required = false) t: String,
             @RequestParam(value = "objectType", required = false) itemTypeId: String,
             @RequestParam(value = "distance", required = false, defaultValue = "100") distance: Int,
             @RequestParam(value = "location", required = false) location: String): ScalapressPage = {

    val page = PageUrlUtils.parse(req)

    val attributeValues = req.getParameterMap.asScala
      .filter(arg => _isAttributeParameter(arg._1.toString))
      .filter(arg => arg._2.asInstanceOf[Array[String]].length > 0)
      .filter(arg => arg._2.asInstanceOf[Array[String]].head.trim.length > 0)
      .map(arg => AttributeSelection(arg._1.toString.drop("attr_".length), arg._2.asInstanceOf[Array[String]].head
    ))

    val search = Search(
      attributeValues = attributeValues,
      name = Option(q).orElse(Option(name)),
      itemTypeId = Option(itemTypeId).map(_.toLong),
      distance = distance,
      location = Option(location),
      page = page,
      sort = sort
    )

    val result = try {
      searchService.search(search)
    } catch {
      case e: Exception =>
        logger.error(e.getMessage)
        SearchResult.apply()
    }
    logger.debug("Search Result {}", result)
    val objects = objectDao.findBulk(result.refs.map(_.id)).filter(obj => Item.STATUS_LIVE.equalsIgnoreCase(obj.status))
    logger.debug("Loaded {} objects", objects.size)

    val sreq = ScalapressRequest(req, context)
      .withTitle("Search Results")
      .withLocation(location)
      .withSearchResult(result)
    val theme = themeService.default
    val spage = ScalapressPage(theme, sreq)

    val plugin = searchPluginDao.get

    if (objects.size == 0) {

      val section = Option(sectionId)
        .flatMap(id => Option(sectionDao.find(id.toLong)))
        .map(_.asInstanceOf[SearchFormSection])
        .map(_.noResultsText)
      val widget = Option(widgetId)
        .flatMap(id => Option(widgetDao.find(id.toLong)))
        .map(_.asInstanceOf[SearchFormWidget])
        .map(_.noResultsText)
      val noResultsText = section.orElse(widget).orElse(Option(plugin.noResultsText))

      spage.body("<!-- search results: no objects found -->")
      noResultsText.foreach(spage body _)

    } else {

      spage.body("<!-- search results: " + objects.size + " objects found -->")

      val p = Page(objects, page.pageNumber, page.pageSize, result.count.toInt)
      val paging = Paging(req, p)

      Option(objects.head.itemType.objectListMarkup) match {
        case None =>
          logger.debug("No markup available")
          spage.body("<!-- search results: no object list markup found -->")

        case Some(markup) =>
          logger.debug("Using markup {}", markup)

          if (paging.totalPages > 1) spage.body(PagingRenderer.render(paging))
          spage.body(MarkupRenderer.renderObjects(objects, markup, sreq))
          if (paging.totalPages > 1) spage.body(PagingRenderer.render(paging))
      }
    }
    spage
  }

  @ResponseBody
  @RequestMapping(Array("saved/{id}"))
  def savedSearch(@PathVariable("id") id: Long) = {
    val saved = savedSearchDao.find(id)
    val response = searchService.search(Search(saved))
    response.toString
  }
}