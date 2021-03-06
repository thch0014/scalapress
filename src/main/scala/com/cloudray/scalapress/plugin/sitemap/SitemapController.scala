package com.cloudray.scalapress.plugin.sitemap

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.IOUtils
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
class SitemapController(context: ScalapressContext) {

  @ResponseBody
  @RequestMapping(value = Array("sitemap"), produces = Array("text/xml"))
  def sitemap(resp: HttpServletResponse) {
    val urls = UrlBuilder.build(context)
    val sitemap = SitemapWriter.write(urls)
    IOUtils.write(sitemap, resp.getOutputStream)
  }
}
