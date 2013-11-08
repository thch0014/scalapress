package com.cloudray.scalapress.plugin.gallery.base

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConverters._
import com.cloudray.scalapress.plugin.gallery.base.GalleryDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/gallery"))
class GalleryListController(galleryDao: GalleryDao) {

  @RequestMapping
  def list = "admin/plugin/gallery/list.vm"

  @RequestMapping(value = Array("create"))
  def create = {
    val g = new Gallery
    g.name = "new gallery"
    galleryDao.save(g)
    "redirect:/backoffice/gallery"
  }

  @ModelAttribute("galleries")
  def galleries = galleryDao.findAll.asJava
}
