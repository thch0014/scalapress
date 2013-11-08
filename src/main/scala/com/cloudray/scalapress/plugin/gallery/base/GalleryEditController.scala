package com.cloudray.scalapress.plugin.gallery.base

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import com.cloudray.scalapress.media.{AssetStore, AssetService}
import scala.collection.JavaConverters._
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.gallery.base.GalleryDao

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/gallery/{id}"))
class GalleryEditController(galleryDao: GalleryDao,
                            assetStore: AssetStore,
                            assetService: AssetService) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("gallery") g: Gallery) = "admin/plugin/gallery/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("gallery") g: Gallery,
           @RequestParam(value = "upload", required = false) upload: MultipartFile,
           req: HttpServletRequest) = {

    g.images = g.images.asScala.map(img =>
      img.copy(description = req.getParameter("desc_" + img.key))
    ).asJava

    if (upload != null && !upload.isEmpty) {
      val key = assetService.add(upload.getOriginalFilename, upload.getInputStream)
      if (key != null) g.images.add(Image(key, null))
    }

    galleryDao.save(g)
    "redirect:/backoffice/plugin/gallery/" + g.id
  }

  @RequestMapping(value = Array("delete/{filename}"))
  def deleteImage(@ModelAttribute("gallery") g: Gallery, @PathVariable("filename") filename: String) = {
    if (g.images.remove(filename)) galleryDao.save(g)
    "redirect:/backoffice/plugin/gallery/" + g.id
  }

  @RequestMapping(value = Array("delete"))
  def delete(@ModelAttribute("gallery") g: Gallery) = {
    galleryDao.remove(g)
    "redirect:/backoffice/plugin/gallery"
  }

  @ModelAttribute("gallery")
  def gallery(@PathVariable("id") id: Long) = galleryDao.find(id)

  @ModelAttribute("images")
  def images(@PathVariable("id") id: Long) = galleryDao.find(id).images

  @ModelAttribute("cdnBaseUrl")
  def cdnBaseUrl = assetStore.baseUrl
}
