package com.cloudray.scalapress.plugin.gallery.galleryview

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.gallery.base.GalleryDao
import scala.collection.JavaConverters._
import com.cloudray.scalapress.section.SectionDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/gallery/galleryview/section/{id}"))
class GalleryViewSectionController(sectionDao: SectionDao,
                                   galleryDao: GalleryDao) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: GalleryViewSection) = "admin/plugin/gallery/galleryview/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: GalleryViewSection, req: HttpServletRequest,
           @RequestParam(value = "upload", required = false) upload: MultipartFile) = {
    sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): GalleryViewSection = sectionDao.find(id).asInstanceOf[GalleryViewSection]

  @ModelAttribute("galleries")
  def galleries = galleryDao.findAll.asJava
}
