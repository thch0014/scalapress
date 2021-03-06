package com.cloudray.scalapress.theme.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, PathVariable, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.theme.{ThemeDao, Theme}

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/theme/{id}"))
class ThemeEditController(themeDao: ThemeDao) {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute theme: Theme) = "admin/theme/edit.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute theme: Theme) = {
    themeDao.save(theme)
    edit(theme)
  }

  @ModelAttribute
  def theme(@PathVariable("id") id: Long) = themeDao.find(id)
}
