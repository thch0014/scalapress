package com.cloudray.scalapress.plugin.attachment

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/attachment/section/{id}"))
class AttachmentSectionController(context: ScalapressContext) {

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(@ModelAttribute("section") section: AttachmentSection) = "admin/plugin/attachment/section/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(@ModelAttribute("section") section: AttachmentSection, req: HttpServletRequest,
           @RequestParam(value = "upload", required = false) upload: MultipartFile) = {

    section.attachments = section.attachments.asScala.map(attachment => {
      val desc = req.getParameter("desc_" + attachment.assetKey)
      Attachment(attachment.assetKey, desc)
    }).asJava

    if (upload != null && !upload.isEmpty) {
      val key = context.assetStore.add(upload.getOriginalFilename, upload.getInputStream)
      if (key != null) section.attachments.add(Attachment(key, null))
    }

    context.sectionDao.save(section)
    edit(section)
  }

  @RequestMapping(value = Array("delete/{key}"), method = Array(RequestMethod.GET))
  def deleteAttachment(@ModelAttribute("section") section: AttachmentSection, @PathVariable("key") key: String) = {
    section.attachments = section.attachments.asScala.filterNot(_.assetKey == key).asJava
    context.sectionDao.save(section)
    "redirect:/backoffice/plugin/attachment/section/" + section.id
  }

  @ModelAttribute("section")
  def section(@PathVariable("id") id: Long): AttachmentSection =
    context.sectionDao.find(id).asInstanceOf[AttachmentSection]
}
