package com.cloudray.scalapress.plugin.gallery.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ExceptionHandler, PathVariable, ResponseBody, RequestMapping}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.{ScalapressContext, ScalapressRequest}
import com.cloudray.scalapress.util.mvc.{ScalapressPage, HttpStatusException}
import com.cloudray.scalapress.theme.ThemeService
import com.cloudray.scalapress.plugin.gallery.galleryview.{GalleryViewRenderer, GalleryDao}

/** @author Stephen Samuel
  *
  *         Special controller for showing a single gallery
  * */
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
                page.body(GalleryViewRenderer.renderGallery(gallery, context.assetStore))
                page
        }
    }

    @ResponseBody
    @RequestMapping
    def view(req: HttpServletRequest): ScalapressPage = {

        val gallery = galleryDao.findAll()
        val theme = themeService.default

        val page = ScalapressPage(theme, ScalapressRequest(req, context))
        page.body("<h1>Galleries</h1>")
        page.body(GalleryViewRenderer.renderCovers(gallery))
        page
    }
}
