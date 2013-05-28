package com.cloudray.scalapress.plugin.carousel.tinycarousel

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import scala.Array
import org.springframework.web.multipart.MultipartFile

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/carousel/tinycarousel/section/{id}"))
class TinyCarouselSectionController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("section") section: TinyCarouselSection) = "admin/plugin/carousel/tinycarousel/section/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("section") section: TinyCarouselSection,
             @RequestParam(value = "upload", required = false) upload: MultipartFile) = {

        if (upload != null && !upload.isEmpty) {
            val key = context.assetStore.add(upload.getOriginalFilename, upload.getInputStream)
            section.images.add(key)
        }

        context.sectionDao.save(section)
        edit(section)
    }

    @RequestMapping(value = Array("delete/{filename}"))
    def deleteImage(@ModelAttribute("section") section: TinyCarouselSection, @PathVariable("filename") filename: String) = {
        section.images.remove(filename)
        context.sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute("section") def section(@PathVariable("id") id: Long): TinyCarouselSection =
        context.sectionDao.find(id).asInstanceOf[TinyCarouselSection]
}
