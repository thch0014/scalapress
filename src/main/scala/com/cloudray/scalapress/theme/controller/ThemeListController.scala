package com.cloudray.scalapress.theme.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.theme.{ThemeDao, Theme}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/theme"))
class ThemeListController {

  @Autowired var themeDao: ThemeDao = _
  @Autowired var context: ScalapressContext = _

  @RequestMapping(produces = Array("text/html"))
  def list = "admin/theme/list.vm"

  @RequestMapping(value = Array("create"), produces = Array("text/html"))
  def create = {
    val theme = new Theme
    theme.name = "new theme"
    themeDao.save(theme)
    "redirect:/backoffice/theme"
  }

  @ModelAttribute("themes") def themes = themeDao.findAll().asJava
}
