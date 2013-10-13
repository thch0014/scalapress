package com.cloudray.scalapress.plugin.gallery.masonry

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import org.springframework.web.multipart.MultipartFile
import com.cloudray.scalapress.plugin.gallery.gallerific.GallerifficSection
import com.cloudray.scalapress.plugin.gallery.GalleryImage
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/gallery/masonry/section/{id}"))
class MasonrySectionController {

  @Autowired var context: ScalapressContext = _

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: MasonrySection) = "admin/plugin/gallery/masonry/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: MasonrySection,
           req: HttpServletRequest,
           @RequestParam(value = "upload", required = false) upload: MultipartFile) = {

    section.images = section.images.asScala.map(img => {
      val desc = req.getParameter("desc_" + img.key)
      GalleryImage(img.key, desc)
    }).asJava

    if (upload != null && !upload.isEmpty) {
      val key = context.assetStore.add(upload.getOriginalFilename, upload.getInputStream)
      section.images.add(GalleryImage(key, null))
    }

    context.sectionDao.save(section)
    edit(section)
  }

  def deleteImage(@ModelAttribute("section") section: MasonrySection,
                  @PathVariable("filename") filename: String) = {
    section.images.remove(filename)
    context.sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section") def section(@PathVariable("id") id: Long): GallerifficSection =
    context.sectionDao.find(id).asInstanceOf[GallerifficSection]
}
