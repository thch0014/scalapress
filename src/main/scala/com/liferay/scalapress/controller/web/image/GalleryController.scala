package com.liferay.scalapress.controller.web.image

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import com.liferay.scalapress.ScalapressContext
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.GalleryDao
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.gallery.GalleryRenderer

/** @author Stephen Samuel
  *
  *         Special controller for showing a single gallery
  * */
@Controller
@RequestMapping(Array("gallery/{id}"))
class GalleryController {

    @Autowired var galleryDao: GalleryDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var themeService: ThemeService = _

    @ResponseBody
    @RequestMapping(value = Array("{id:\\d+}"), produces = Array("text/html"))
    def view(@PathVariable("id") id: Long, req: HttpServletRequest): ScalaPressPage = {

        val gallery = galleryDao.find(id)
        val theme = themeService.default
        val page = ScalaPressPage(theme, req)

        val render = GalleryRenderer.renderGallery(gallery)
        page.body(render)
        page
    }
}
