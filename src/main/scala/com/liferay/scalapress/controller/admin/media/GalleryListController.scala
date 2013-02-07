package com.liferay.scalapress.controller.admin.media

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.dao.GalleryDao
import scala.Array
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.gallery.Gallery

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/gallery"))
class GalleryListController {

    @Autowired var galleryDao: GalleryDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def list = "admin/gallery/list.vm"

    @RequestMapping(value = Array("create"), produces = Array("text/html"))
    def create = {
        val g = new Gallery
        g.name = "new gallery"
        galleryDao.save(g)
        "redirect:/backoffice/gallery"
    }

    import scala.collection.JavaConverters._

    @ModelAttribute("galleries") def galleries = galleryDao.findAll().asJava
}
