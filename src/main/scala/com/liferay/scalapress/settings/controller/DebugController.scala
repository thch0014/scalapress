package com.liferay.scalapress.settings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.search.SearchService
import com.liferay.scalapress.settings.Debug
import scala.collection.JavaConverters._
import scala.collection.immutable.TreeMap

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/debug"))
class DebugController {

    @Autowired var searchService: SearchService = _

    @RequestMapping
    def debug(request: HttpServletRequest) = "admin/settings/debug.vm"

    @ModelAttribute("properties") def properties(request: HttpServletRequest): java.util.Map[String, String] = {
        val properties = Debug.map ++ searchService.stats ++ System.getProperties.asScala ++ System.getenv().asScala
        val http = Map("request.http.cookies" -> request.getCookies.mkString(","),
            "request.http.headers" -> request.getHeaderNames.toString)
        val sorted = TreeMap[String, String]() ++ properties ++ http
        sorted.asJava
    }
}