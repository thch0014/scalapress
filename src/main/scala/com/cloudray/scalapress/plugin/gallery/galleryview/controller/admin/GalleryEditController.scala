package com.cloudray.scalapress.plugin.gallery.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import org.springframework.web.multipart.MultipartFile
import com.cloudray.scalapress.plugin.gallery.galleryview.{GalleryDao, Gallery}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/gallery/{id}"))
class GalleryEditController {

  @Autowired var galleryDao: GalleryDao = _
  @Autowired var context: ScalapressContext = _

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("gallery") g: Gallery) = "admin/gallery/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("gallery") g: Gallery,
           @RequestParam(value = "upload", required = false) file: MultipartFile) = {
    if (file != null && !file.isEmpty) {
      val key = context.assetStore.add(file.getOriginalFilename, file.getInputStream)
      g.images.add(key)
    }
    galleryDao.save(g)
    edit(g)
  }

  @RequestMapping(value = Array("delete/{filename}"))
  def deleteImage(@ModelAttribute("gallery") g: Gallery, @PathVariable("filename") filename: String) = {
    if (g.images.remove(filename)) galleryDao.save(g)
    "redirect:/backoffice/gallery/" + g.id
  }

  @RequestMapping(value = Array("delete"))
  def delete(@ModelAttribute("gallery") g: Gallery) = {
    galleryDao.remove(g)
    "redirect:/backoffice/gallery"
  }

  @ModelAttribute("gallery") def gallery(@PathVariable("id") id: Long) = galleryDao.find(id)
}
