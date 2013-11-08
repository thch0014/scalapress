package com.cloudray.scalapress.plugin.gallery.masonry

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.media.Image

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/gallery/masonry/section/{id}"))
class MasonrySectionController(context: ScalapressContext) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: MasonrySection) = "admin/plugin/gallery/masonry/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: MasonrySection, req: HttpServletRequest,
           @RequestParam(value = "upload", required = false) upload: MultipartFile) = {

    section.images = section.images.asScala.map(img => {
      val desc = req.getParameter("desc_" + img.key)
      Image(img.key, desc)
    }).asJava

    if (upload != null && !upload.isEmpty) {
      val key = context.assetStore.add(upload.getOriginalFilename, upload.getInputStream)
      if (key != null) {
        section.images.add(Image(key, null))
      }
    }

    context.sectionDao.save(section)
    edit(section)
  }

  @RequestMapping(value = Array("delete/{key}"), method = Array(RequestMethod.GET))
  def deleteImage(@ModelAttribute("section") section: MasonrySection, @PathVariable("key") key: String) = {
    section.images = section.images.asScala.filterNot(_.key == key).asJava
    context.sectionDao.save(section)
    "redirect:/backoffice/plugin/gallery/masonry/section/" + section.id
  }

  @ModelAttribute("section") def section(@PathVariable("id") id: Long): MasonrySection =
    context.sectionDao.find(id).asInstanceOf[MasonrySection]
}
