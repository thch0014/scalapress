package com.cloudray.scalapress.plugin.gallery.gallerific

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import org.springframework.web.multipart.MultipartFile

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/galleriffic/section/{id}"))
class GallerifficSectionController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("section") section: GallerifficSection) = "admin/plugin/gallery/galleriffic/section/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("section") section: GallerifficSection,
             @RequestParam(value = "upload", required = false) upload: MultipartFile) = {

        if (upload != null && !upload.isEmpty) {
            val key = context.assetStore.add(upload.getOriginalFilename, upload.getInputStream)
            section.images.add(key)
        }

        context.sectionDao.save(section)
        edit(section)
    }

    @RequestMapping(value = Array("delete/{filename}"))
    def deleteImage(@ModelAttribute("section") section: GallerifficSection, @PathVariable("filename") filename: String) = {
        section.images.remove(filename)
        context.sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute("section") def section(@PathVariable("id") id: Long): GallerifficSection =
        context.sectionDao.find(id).asInstanceOf[GallerifficSection]
}
