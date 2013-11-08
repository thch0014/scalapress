package com.cloudray.scalapress.plugin.gallery.masonry

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestParam, RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.framework.ScalapressContext

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

    context.sectionDao.save(section)
    edit(section)
  }

  @ModelAttribute("section") def section(@PathVariable("id") id: Long): MasonrySection =
    context.sectionDao.find(id).asInstanceOf[MasonrySection]
}
