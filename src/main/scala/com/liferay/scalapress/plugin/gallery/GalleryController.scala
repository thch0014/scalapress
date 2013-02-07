package com.liferay.scalapress.plugin.gallery

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{PathVariable, ResponseBody, RequestMapping}
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.controller.web.ScalaPressPage
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.GalleryDao
import com.liferay.scalapress.service.theme.ThemeService
import com.liferay.scalapress.ScalapressRequest

/** @author Stephen Samuel
  *
  *         Special controller for showing a single gallery
  **/
@Controller
@RequestMapping(Array("gallery"))
class GalleryController {

    @Autowired var galleryDao: GalleryDao = _
    @Autowired var themeService: ThemeService = _

    @ResponseBody
    @RequestMapping(value = Array("{id}"), produces = Array("text/html"))
    def view(@PathVariable("id") id: Long, req: HttpServletRequest): ScalaPressPage = {

        val gallery = galleryDao.find(id)
        val theme = themeService.default
        val sreq = ScalapressRequest(req).withTitle(gallery.name)

        val page = ScalaPressPage(theme, sreq)
        page.body("<h1>" + gallery.name + "</h1>")
        page.body(GalleryRenderer.renderGallery(gallery))
        page
    }

    @ResponseBody
    @RequestMapping(produces = Array("text/html"))
    def view(req: HttpServletRequest): ScalaPressPage = {

        val gallery = galleryDao.findAll()
        val theme = themeService.default

        val page = ScalaPressPage(theme, req)
        page.body("<h1>Galleries</h1>")
        page.body(GalleryRenderer.renderCovers(gallery))
        page
    }
}