package com.liferay.scalapress.controller.web.image

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.GalleryDao
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.plugin.gallery.GalleryRenderer

/** @author Stephen Samuel
  *
  *         Special controller for showing a single gallery
  **/
@Controller
@RequestMapping(Array("gallery/{id}"))
class GalleryController {

    @Autowired var galleryDao: GalleryDao = _
    @Autowired var themeService: ThemeService = _

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def view(@PathVariable("id") id: Long, req: HttpServletRequest): ScalaPressPage = {

        val gallery = galleryDao.find(id)
        val theme = themeService.default

        val render = GalleryRenderer.renderGallery(gallery)

        val page = ScalaPressPage(theme, req)
        page.body("<h1>" + gallery.name + "</h1>")
        page.body(render)
        page
    }
}
