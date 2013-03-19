package com.liferay.scalapress.plugin.gallery.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import org.springframework.security.authentication.encoding.PasswordEncoder
import org.springframework.web.multipart.MultipartFile
import com.liferay.scalapress.service.asset.AssetStore
import com.liferay.scalapress.plugin.gallery.Gallery
import com.liferay.scalapress.media.{Image, GalleryDao}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/gallery/{id}"))
class GalleryEditController {

    @Autowired var assetStore: AssetStore = _
    @Autowired var galleryDao: GalleryDao = _
    @Autowired var context: ScalapressContext = _
    @Autowired var passwordEncoder: PasswordEncoder = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("gallery") g: Gallery) = "admin/gallery/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("gallery") g: Gallery,
             @RequestParam(value = "upload", required = false) file: MultipartFile) = {
        if (file != null && !file.isEmpty) {
            val key = assetStore.add(file.getOriginalFilename, file.getInputStream)
            val image = new Image
            image.filename = key
            image.name = file.getOriginalFilename
            image.date = System.currentTimeMillis()
            image.gallery = g
            g.images.add(image)
        }

        galleryDao.save(g)
        edit(g)
    }

    @RequestMapping(value = Array("delete"))
    def delete(@ModelAttribute("gallery") g: Gallery) = {
        galleryDao.remove(g)
        "redirect:/backoffice/gallery"
    }

    @ModelAttribute("gallery") def gallery(@PathVariable("id") id: Long) = galleryDao.find(id)
}
