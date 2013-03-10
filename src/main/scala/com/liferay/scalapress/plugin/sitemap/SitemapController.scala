package com.liferay.scalapress.plugin.sitemap

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import com.liferay.scalapress.ScalapressContext
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletResponse
import org.jdom.output.{XMLOutputter, Format}

/** @author Stephen Samuel */
@Controller
class SitemapController {

    @Autowired var context: ScalapressContext = _

    @ResponseBody
    @RequestMapping(value = Array("sitemap"), produces = Array("text/xml"))
    def sitemap(resp: HttpServletResponse) {
        val urls = UrlBuilder.build(context)
        val doc = SitemapWriter.write(urls)
        new XMLOutputter(Format.getPrettyFormat).output(doc, resp.getOutputStream)
    }
}
