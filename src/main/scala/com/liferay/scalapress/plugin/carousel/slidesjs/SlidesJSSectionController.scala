package com.liferay.scalapress.plugin.carousel.slidesjs

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import scala.Array
import org.springframework.web.multipart.MultipartFile

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/carousel/slidesjs/section/{id}"))
class SlidesJSSectionController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(@ModelAttribute("section") section: SlidesJSSection) = "admin/plugin/carousel/slidesjs/section/edit.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(@ModelAttribute("section") section: SlidesJSSection,
             @RequestParam(value = "upload", required = false) upload: MultipartFile) = {

        if (upload != null && !upload.isEmpty) {
            val key = context.assetStore.add(upload.getOriginalFilename, upload.getInputStream)
            section.images.add(key)
        }

        context.sectionDao.save(section)
        edit(section)
    }

    @RequestMapping(value = Array("delete/{filename}"))
    def deleteImage(@ModelAttribute("section") section: SlidesJSSection, @PathVariable("filename") filename: String) = {
        section.images.remove(filename)
        context.sectionDao.save(section)
        edit(section)
    }

    @ModelAttribute("section") def section(@PathVariable("id") id: Long): SlidesJSSection =
        context.sectionDao.find(id).asInstanceOf[SlidesJSSection]

}
