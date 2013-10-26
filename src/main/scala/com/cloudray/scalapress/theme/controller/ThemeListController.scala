package com.cloudray.scalapress.theme.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.theme.{ThemeImporter, ThemeDao, Theme}
import scala.collection.JavaConverters._
import scala.Array
import org.springframework.web.multipart.MultipartFile
import org.apache.commons.io.FilenameUtils

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/theme"))
@Autowired
class ThemeListController(themeDao: ThemeDao,
                          themeImporter: ThemeImporter) {

  @RequestMapping(produces = Array("text/html"))
  def list = "admin/theme/list.vm"

  @RequestMapping(value = Array("create"), produces = Array("text/html"))
  def create = {
    val theme = new Theme
    theme.name = "new theme"
    themeDao.save(theme)
    "redirect:/backoffice/theme"
  }

  @RequestMapping(value = Array("{id}/delete"))
  def delete(@PathVariable("id") id: Long): String = {
    themeDao.removeById(id)
    "redirect:/backoffice/theme"
  }

  @RequestMapping(value = Array("upload"), method = Array(RequestMethod.POST))
  def upload(@RequestParam("upload") upload: MultipartFile): String = {

    val name = FilenameUtils.getBaseName(upload.getOriginalFilename)
    val in = upload.getInputStream

    val theme = themeImporter.importTheme(name, in)
    themeDao.save(theme)

    "redirect:/backoffice/theme"
  }

  @ModelAttribute("themes")
  def themes = themeDao.findAll().asJava
}
