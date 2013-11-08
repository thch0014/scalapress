package com.cloudray.scalapress.plugin.gallery.masonry

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.gallery.base.GalleryDao
import scala.collection.JavaConverters._
import com.cloudray.scalapress.section.SectionDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/gallery/masonry/section/{id}"))
class MasonrySectionController(sectionDao: SectionDao,
                               galleryDao: GalleryDao) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: MasonrySection) = "admin/plugin/gallery/masonry/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: MasonrySection) = {
    sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): MasonrySection = sectionDao.find(id).asInstanceOf[MasonrySection]

  @ModelAttribute("galleries")
  def galleries = galleryDao.findAll.asJava
}
