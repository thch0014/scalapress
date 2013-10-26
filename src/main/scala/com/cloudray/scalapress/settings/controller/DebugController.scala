package com.cloudray.scalapress.settings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.search.SearchService
import com.cloudray.scalapress.settings.Debug
import scala.collection.JavaConverters._
import scala.collection.immutable.TreeMap

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/debug"))
@Autowired
class DebugController(searchService: SearchService) {

  @RequestMapping
  def debug(request: HttpServletRequest) = "admin/settings/debug.vm"

  @ModelAttribute("properties")
  def properties(request: HttpServletRequest): java.util.Map[String, String] = {
    val properties = Debug.map ++ searchService.stats ++ System.getProperties.asScala ++ System.getenv().asScala
    val http = Map("sreq.http.cookies" -> request.getCookies.mkString(","),
      "sreq.http.headers" -> request.getHeaderNames.toString)
    val sorted = TreeMap[String, String]() ++ properties ++ http
    sorted.asJava
  }
}
