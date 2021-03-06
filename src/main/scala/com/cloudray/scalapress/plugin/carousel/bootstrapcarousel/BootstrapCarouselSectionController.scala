package com.cloudray.scalapress.plugin.carousel.bootstrapcarousel

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import org.springframework.web.multipart.MultipartFile
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/carousel/bootstrapcarousel/section/{id}"))
class BootstrapCarouselSectionController(context: ScalapressContext) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: BootstrapCarouselSection) =
    "admin/plugin/carousel/bootstrapcarousel/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: BootstrapCarouselSection,
           @RequestParam(value = "upload", required = false) upload: MultipartFile) = {

    if (upload != null && !upload.isEmpty) {
      val key = context.assetStore.add(upload.getOriginalFilename, upload.getInputStream)
      section.images.add(key)
    }

    context.sectionDao.save(section)
    edit(section)
  }

  @RequestMapping(value = Array("delete/{filename}"))
  def deleteImage(@ModelAttribute("section") section: BootstrapCarouselSection,
                  @PathVariable("filename") filename: String) = {
    section.images.remove(filename)
    context.sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): BootstrapCarouselSection =
    context.sectionDao.find(id).asInstanceOf[BootstrapCarouselSection]
}
