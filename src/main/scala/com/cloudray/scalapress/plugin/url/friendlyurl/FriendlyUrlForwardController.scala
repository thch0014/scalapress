package com.cloudray.scalapress.plugin.url.friendlyurl

import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping, PathVariable}
import org.springframework.stereotype.Controller
import javax.servlet.http.HttpServletResponse

/** @author Stephen Samuel */
@Controller
@RequestMapping
class FriendlyUrlForwardController {

  @ResponseBody
  @deprecated("backwards compatibility only", "0.41")
  @RequestMapping(Array("object-{id:\\d+}-{name}"))
  def obj(@PathVariable("id") id: Int, @PathVariable("name") name: String, resp: HttpServletResponse): Unit = {
    resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY)
    resp.setHeader("Location", s"/item-$id-$name")
  }

  @RequestMapping(Array("item-{id:\\d+}-{name}"))
  def item(@PathVariable("id") id: Int): String = "forward:/item/" + id

  @RequestMapping(Array("folder-{id:\\d+}-{name}"))
  def folder(@PathVariable("id") id: Int): String = "forward:/folder/" + id
}
