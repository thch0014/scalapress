package com.liferay.scalapress.search.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{TypeDao, ObjectDao}
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext, Logging}
import javax.annotation.PostConstruct
import actors.Futures
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.service.theme.{MarkupRenderer, ThemeService}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.domain.attr.AttributeValue
import com.liferay.scalapress.domain.attr.Attribute
import scala.collection.JavaConverters._
import com.liferay.scalapress.search.{SavedSearch, SearchPluginDao, SearchService, SavedSearchDao}
import com.liferay.scalapress.section.PluginDao
import com.liferay.scalapress.search.section.SearchFormSection
import com.sksamuel.scoot.soa.Page
import com.liferay.scalapress.enums.Sort

/** @author Stephen Samuel */

@Controller
@RequestMapping(Array("search"))
class SearchController extends Logging {

    val PageSize = 20

    @Autowired var savedSearchDao: SavedSearchDao = _
    @Autowired var searchService: SearchService = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var context: ScalapressContext = _
    @Autowired var searchPluginDao: SearchPluginDao = _
    @Autowired var pluginDao: PluginDao = _

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def search(req: HttpServletRequest,
               @RequestParam(value = "sort", required = false) sort: Sort,
               @RequestParam(value = "sectionId", required = false) sectionId: String,
               @RequestParam(value = "q", required = false) q: String,
               @RequestParam(value = "type", required = false) t: String): ScalaPressPage = {

        val plugin = searchPluginDao.get

        val attributeValues = req.getParameterMap.asScala
          .filter(arg => arg._1.toString.startsWith("attr_"))
          .filter(arg => arg._2.asInstanceOf[Array[String]].length > 0)
          .filter(arg => arg._2.asInstanceOf[Array[String]].head.trim.length > 0)
          .map(arg => {
            val av = new AttributeValue
            av.attribute = new Attribute
            av.attribute.id = arg._1.toString.drop(5).toLong
            av.value = arg._2.asInstanceOf[Array[String]].head
            av
        })

        val search = new SavedSearch
        search.attributeValues = attributeValues.toSet.asJava
        search.keywords = q
        search.maxResults = PageSize
        search.objectType = Option(t).map(t => typeDao.find(t.toLong)).orNull
        search.sortType = sort

        val response = searchService.search(search)
        val objects = response.hits.hits().map(hit => {
            val id = hit.id().toLong
            objectDao.find(id)
        }).toList

        // val objects = objectDao.search(new Search(classOf[Obj]).addFilterIn("id", ids.toSeq.asJava))

        val sreq = ScalapressRequest(req, context).withTitle("Search Results")
        val theme = themeService.default
        val page = ScalaPressPage(theme, sreq)

        if (objects.size == 0) {

            val noResults = Option(sectionId).map(_.toLong)
              .flatMap(id => Option(pluginDao.find(id)))
              .flatMap(section => Option(section.asInstanceOf[SearchFormSection].noResultsText))
              .getOrElse(plugin.noResultsText)

            page.body("<!-- search results: no objects found -->")
            page.body(noResults)

        } else {

            page.body("<!-- search results: " + objects.size + " objects found -->")

            val paging = Page(objects, 1, PageSize, response.hits.totalHits.toInt)

            val markup = objects.head.objectType.objectListMarkup
            if (markup != null) {
                page.body("<!-- search results: no object list markup found -->")
                page.body(MarkupRenderer.renderObjects(objects, markup, sreq, context))
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

    @PostConstruct
    def index() {
        Futures.future {
            searchService.index()
        }
    }
}