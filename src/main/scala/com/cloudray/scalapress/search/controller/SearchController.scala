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
import com.cloudray.scalapress.enums.Sort
import com.cloudray.scalapress.obj.{Obj, ObjectDao, TypeDao}
import com.cloudray.scalapress.obj.attr.{AttributeValue, Attribute}
import com.cloudray.scalapress.util.mvc.ScalapressPage
import com.cloudray.scalapress.theme.{ThemeService, MarkupRenderer}
import com.sksamuel.scoot.soa.{Paging, Page}
import scala.Some

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
    @Autowired var pluginDao: SectionDao = _

    @ResponseBody
    @RequestMapping(value = Array("count"), produces = Array("text/html"))
    def count = searchService.count.toString

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def search(req: HttpServletRequest,
               @RequestParam(value = "id", required = false, defaultValue = "0") id: Long,
               @RequestParam(value = "sort", required = false) sort: Sort,
               @RequestParam(value = "sectionId", required = false) sectionId: String,
               @RequestParam(value = "q", required = false) q: String,
               @RequestParam(value = "type", required = false) t: String,
               @RequestParam(value = "objectType", required = false) objectTypeId: String,
               @RequestParam(value = "distance", required = false, defaultValue = "100") distance: Int,
               @RequestParam(value = "location", required = false) location: String): ScalapressPage = {

        val objects = if (id > 0) {

            Option(objectDao.find(id)) match {
                case Some(obj) if obj.status.equalsIgnoreCase("live") => List(obj)
                case _ => Nil
            }

        } else {

            val attributeValues = req.getParameterMap.asScala
              .filter(arg => arg._1.toString.startsWith("attr_"))
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
            search.maxResults = PageSize
            search.objectType = Option(t).orElse(Option(objectTypeId)).map(t => typeDao.find(t.toLong)).orNull
            search.sortType = sort

            val result = searchService.search(search)
            val objs = result.refs.map(ref => objectDao.find(ref.id)).toList
            val live = objs.filter(obj => Obj.STATUS_LIVE.equalsIgnoreCase(obj.status))

            live
        }

        // val objects = objectDao.search(new Search(classOf[Obj]).addFilterIn("id", ids.toSeq.asJava))

        val sreq = ScalapressRequest(req, context).withTitle("Search Results").withLocation(location)
        val theme = themeService.default
        val page = ScalapressPage(theme, sreq)

        val plugin = searchPluginDao.get

        if (objects.size == 0) {

            val noResults = Option(sectionId).map(_.toLong)
              .flatMap(id => Option(pluginDao.find(id)))
              .flatMap(section => Option(section.asInstanceOf[SearchFormSection].noResultsText))
              .getOrElse(plugin.noResultsText)

            page.body("<!-- search results: no objects found -->")
            if (noResults != null)
                page.body(noResults)

        } else {

            page.body("<!-- search results: " + objects.size + " objects found -->")

            val p = Page(objects, 1, PageSize, objects.size)
            val paging = Paging(req, p)

            val markup = objects.head.objectType.objectListMarkup
            if (markup == null) {
                page.body("<!-- search results: no object list markup found -->")
            } else {
                if (paging.totalPages > 1)
                    page.body(PagingRenderer.render(paging))
                page.body(MarkupRenderer.renderObjects(objects, markup, sreq))
                if (paging.totalPages > 1)
                    page.body(PagingRenderer.render(paging))
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