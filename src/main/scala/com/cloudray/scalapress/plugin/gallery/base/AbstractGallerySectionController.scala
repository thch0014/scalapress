package com.cloudray.scalapress.plugin.gallery.base

import org.springframework.web.bind.annotation.ModelAttribute
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
abstract class AbstractGallerySectionController(galleryDao: GalleryDao) {

  @ModelAttribute("galleries")
  def galleries = galleryDao.findAll.asJava
}
