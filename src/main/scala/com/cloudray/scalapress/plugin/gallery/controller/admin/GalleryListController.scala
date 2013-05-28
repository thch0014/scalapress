package com.cloudray.scalapress.plugin.gallery.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.plugin.gallery.Gallery
import scala.collection.JavaConverters._
import com.cloudray.scalapress.media.GalleryDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/gallery"))
class GalleryListController {

    @Autowired var galleryDao: GalleryDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping
    def list = "admin/gallery/list.vm"

    @RequestMapping(value = Array("create"))
    def create = {
        val g = new Gallery
        g.name = "new gallery"
        galleryDao.save(g)
        "redirect:/backoffice/gallery"
    }

    @ModelAttribute("galleries") def galleries = galleryDao.findAll().asJava
}
