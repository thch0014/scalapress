package com.liferay.scalapress.plugin.search.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.{TypeDao, ObjectDao}
import com.liferay.scalapress.{Page, ScalapressRequest, ScalapressContext, Logging}
import javax.annotation.PostConstruct
import actors.Futures
import com.liferay.scalapress.plugin.search.{SearchPluginDao, SearchService}
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.service.theme.{MarkupRenderer, ThemeService}
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */

@Controller
@RequestMapping(Array("search"))
class SearchController extends Logging {

    val PageSize = 20

    @Autowired var service: SearchService = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var typeDao: TypeDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var context: ScalapressContext = _
    @Autowired var searchPluginDao: SearchPluginDao = _

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def search(req: HttpServletRequest,
               @RequestParam("q") q: String,
               @RequestParam(value = "type", required = false) t: String): ScalaPressPage = {

        val sreq = ScalapressRequest(req, context)
        val response = service.search(q, PageSize)
        val objects = response.hits.hits().map(hit => {
            val id = hit.id().toLong
            objectDao.find(id)
        }).toList

        val paging = Page(objects, 1, PageSize, response.hits.totalHits.toInt)

        val markup = searchPluginDao.get.markup
        val theme = themeService.default
        val page = ScalaPressPage(theme, req, context)

        page.body("<h1>Search Results</h1>")
        page.body(PagingRenderer.render(paging))
        if (markup != null)
            page.body(MarkupRenderer.renderObjects(objects, markup, sreq, context))
        page
    }

    @ResponseBody
    @RequestMapping(Array("{type}"))
    def test(@PathVariable("type") typeId: Long, @RequestParam("q") q: String) = {
        val t = typeDao.find(typeId)
        val response = service.searchType(q, t, 50)
        response.toString
    }

    @PostConstruct
    def index() {
        Futures.future {
            service.index()
        }
    }
}