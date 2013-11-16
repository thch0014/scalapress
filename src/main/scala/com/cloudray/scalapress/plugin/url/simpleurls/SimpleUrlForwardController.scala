package com.cloudray.scalapress.plugin.url.simpleurls

import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping, PathVariable}
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletResponse

/** @author Stephen Samuel */
@Controller
@RequestMapping
class SimpleUrlForwardController {

  @deprecated
  @ResponseBody
  @RequestMapping(Array("o{id:\\d+}-{name}"))
  def obj(@PathVariable("id") id: Int, @PathVariable("name") name: String, resp: HttpServletResponse): Unit = {
    resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY)
    resp.setHeader("Location", s"/i$id-$name")
  }

  @RequestMapping(Array("i{id:\\d+}-{name}"))
  def item(@PathVariable("id") id: Int): String = "forward:/item/" + id

  @RequestMapping(Array("f{id:\\d+}-{name}"))
  def folder(@PathVariable("id") id: Int): String = "forward:/folder/" + id
}
