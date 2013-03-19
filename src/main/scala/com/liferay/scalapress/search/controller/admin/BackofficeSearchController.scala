package com.liferay.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import org.springframework.ui.ModelMap
import com.liferay.scalapress.search.SearchService
import com.liferay.scalapress.obj.{ObjectDao, TypeDao}
import com.liferay.scalapress.util.mvc.UrlResolver

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search"))
class BackofficeSearchController {

    @Autowired var service: SearchService = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var typeDao: TypeDao = _

    @RequestMapping
    def test(@RequestParam("q") q: String, model: ModelMap) = {

        val response = service.search(q, 50)
        val results = response.hits().hits().map(hit => {

            val `type` = hit.getType.toLowerCase match {
                case s: String if s.matches("\\d+") => typeDao.find(s.toLong).name
                case _ => "unknown"
            }

            SearchResult(hit.id().toLong,
                `type`,
                hit.field("name").value(),
                UrlResolver.objectEdit(hit.id.toLong),
                UrlResolver.objectSiteView(hit.id.toLong))
        })

        model.put("results", results.toList.asJava)
        "admin/search/results.vm"
    }
}

case class SearchResult(id: Long, t: String, name: String, editUrl: String, viewUrl: String)
