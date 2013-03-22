package com.liferay.scalapress.plugin.gallery.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ExceptionHandler, PathVariable, ResponseBody, RequestMapping}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.plugin.gallery.GalleryRenderer
import com.liferay.scalapress.media.GalleryDao
import com.liferay.scalapress.util.mvc.{ScalapressPage, HttpStatusException}
import com.liferay.scalapress.theme.ThemeService

/** @author Stephen Samuel
  *
  *         Special controller for showing a single gallery
  **/
@Controller
@RequestMapping(Array("gallery"))
class GalleryController {

    @Autowired var galleryDao: GalleryDao = _
    @Autowired var themeService: ThemeService = _
    @Autowired var context: ScalapressContext = _

    @ExceptionHandler(Array(classOf[HttpStatusException]))
    @ResponseBody
    def exception(resp: HttpServletResponse, e: HttpStatusException) {
        resp.setStatus(e.status)
    }

    @ResponseBody
    @RequestMapping(value = Array("{id}"), produces = Array("text/html"))
    def view(@PathVariable("id") id: Long, req: HttpServletRequest): ScalapressPage = {

        Option(galleryDao.find(id)) match {
            case None => throw new HttpStatusException(404)
            case Some(gallery) =>
                val theme = themeService.default
                val sreq = ScalapressRequest(req, context).withTitle(gallery.name)

                val page = ScalapressPage(theme, sreq)
                page.body("<h1>" + gallery.name + "</h1>")
                page.body(GalleryRenderer.renderGallery(gallery, context.assetStore))
                page
        }
    }

    @ResponseBody
    @RequestMapping
    def view(req: HttpServletRequest): ScalapressPage = {

        val gallery = galleryDao.findAll()
        val theme = themeService.default

        val page = ScalapressPage(theme, req, context)
        page.body("<h1>Galleries</h1>")
        page.body(GalleryRenderer.renderCovers(gallery))
        page
    }
}
