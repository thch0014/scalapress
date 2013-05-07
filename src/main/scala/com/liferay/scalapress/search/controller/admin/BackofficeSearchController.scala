package com.liferay.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.ModelMap
import com.liferay.scalapress.search.{SavedSearch, SearchService}
import com.liferay.scalapress.obj.{ObjectDao, TypeDao}
import com.liferay.scalapress.util.mvc.UrlResolver
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/search"))
class BackofficeSearchController {

    @Autowired var service: SearchService = _
    @Autowired var objectDao: ObjectDao = _
    @Autowired var typeDao: TypeDao = _

    @RequestMapping
    def test(@RequestParam("q") q: String, model: ModelMap) = {

        val s = new SavedSearch
        s.name = q
        s.maxResults = 50

        val refs = service.search(s)
        val results = refs.map(ref => {

            SearchResult(ref.id,
                ref.objectType,
                ref.name,
                UrlResolver.objectEdit(ref.id),
                UrlResolver.objectSiteView(ref.id))
        })

        model.put("results", results.asJava)
        "admin/search/results.vm"
    }
}

case class SearchResult(id: Long, t: Long, name: String, editUrl: String, viewUrl: String)
