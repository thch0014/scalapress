package com.cloudray.scalapress.plugin.gallery.vegas

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.plugin.gallery.base.{AbstractGallerySectionController, GalleryDao}
import com.cloudray.scalapress.section.SectionDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/gallery/vegas/section/{id}"))
class VegasSectionController(sectionDao: SectionDao,
                             galleryDao: GalleryDao)
  extends AbstractGallerySectionController(galleryDao: GalleryDao) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: VegasSection) = "admin/plugin/gallery/vegas/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: VegasSection) = {
    sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): VegasSection = sectionDao.find(id).asInstanceOf[VegasSection]
}
