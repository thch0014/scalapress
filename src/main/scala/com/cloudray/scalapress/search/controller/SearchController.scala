package com.cloudray.scalapress.search.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.{ScalapressRequest, ScalapressContext, Logging}
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import com.cloudray.scalapress.search._
import com.cloudray.scalapress.section.SectionDao
import com.cloudray.scalapress.search.section.SearchFormSection
import com.cloudray.scalapress.item.{Item, ItemDao, TypeDao}
import com.cloudray.scalapress.item.attr.{AttributeValue, Attribute}
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.theme.{ThemeService, MarkupRenderer}
import com.sksamuel.scoot.soa.{Paging, Page}
import com.cloudray.scalapress.widgets.WidgetDao
import com.cloudray.scalapress.search.widget.SearchFormWidget

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
             @RequestParam(value = "type", required = false) t: String,
             @RequestParam(value = "objectType", required = false) objectTypeId: String,
             @RequestParam(value = "distance", required = false, defaultValue = "100") distance: Int,
             @RequestParam(value = "location", required = false) location: String,
             @RequestParam(value = "pageSize", required = false, defaultValue = "20") pageSize: Int,
             @RequestParam(value = "pageNumber",
               required = false,
               defaultValue = "1") pageNumber: Int): ScalapressPage = {

    val attributeValues = req.getParameterMap.asScala
      .filter(arg => _isAttributeParameter(arg._1.toString))
      .filter(arg => arg._2.asInstanceOf[Array[String]].length > 0)
      .filter(arg => arg._2.asInstanceOf[Array[String]].head.trim.length > 0)
      .map(arg => {
      val av = new AttributeValue
      av.attribute = new Attribute
      av.attribute.id = arg._1.toString.drop("attr_".length).toLong
      av.value = arg._2.asInstanceOf[Array[String]].head
      av
    })

    val search = new SavedSearch
    search.attributeValues = attributeValues.toSet.asJava
    search.keywords = q
    search.distance = distance
    search.location = location
    search.maxResults = pageSize
    search.pageNumber = pageNumber
    search.sortType = sort

    try {
      Option(t).orElse(Option(objectTypeId)).map(arg => typeDao.find(arg.toLong)) match {
        case None =>
        case Some(itemType) =>
          search.objectType = itemType
          search.facets = itemType.attributes.asScala.filter(_.facet).map(_.id.toString).toSeq
      }
    } catch {
      case e: Exception => // unparsable number
    }

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
    val page = ScalapressPage(theme, sreq)

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

      page.body("<!-- search results: no objects found -->")
      noResultsText.foreach(page body _)

    } else {

      page.body("<!-- search results: " + objects.size + " objects found -->")

      val p = Page(objects, pageNumber, pageSize, result.count.toInt)
      val paging = Paging(req, p)

      Option(objects.head.objectType.objectListMarkup) match {
        case None =>
          logger.debug("No markup available")
          page.body("<!-- search results: no object list markup found -->")

        case Some(markup) =>
          logger.debug("Using markup {}", markup)

          if (paging.totalPages > 1) page.body(PagingRenderer.render(paging))
          page.body(MarkupRenderer.renderObjects(objects, markup, sreq))
          if (paging.totalPages > 1) page.body(PagingRenderer.render(paging))
      }
    }
    page
  }

  @ResponseBody
  @RequestMapping(Array("saved/{id}"))
  def savedSearch(@PathVariable("id") id: Long) = {
    val savedSearch = savedSearchDao.find(id)
    val response = searchService.search(savedSearch)
    response.toString
  }
}