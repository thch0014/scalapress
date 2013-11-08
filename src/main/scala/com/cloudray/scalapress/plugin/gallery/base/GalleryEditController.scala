package com.cloudray.scalapress.plugin.gallery.base

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import com.cloudray.scalapress.plugin.gallery.galleryview.GalleryDao
import com.cloudray.scalapress.media.AssetService

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/gallery/{id}"))
class GalleryEditController(galleryDao: GalleryDao,
                            assetService: AssetService) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("gallery") g: Gallery) = "admin/plugin/gallery/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("gallery") g: Gallery,
           @RequestParam(value = "upload", required = false) file: MultipartFile) = {
    if (file != null && !file.isEmpty) {
      val key = assetService.add(file.getOriginalFilename, file.getInputStream)
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
