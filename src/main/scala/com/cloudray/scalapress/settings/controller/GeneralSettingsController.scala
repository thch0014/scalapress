package com.cloudray.scalapress.settings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.settings.{Installation, GeneralSettings}
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/settings/general"))
@Autowired
class GeneralSettingsController(context: ScalapressContext) {

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute("settings") gsettings: GeneralSettings,
           @ModelAttribute site: Installation) = "admin/settings/general.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute("settings") gsettings: GeneralSettings, @ModelAttribute site: Installation) = {
    context.generalSettingsDao.save(gsettings)
    edit(gsettings, site)
  }

  @ModelAttribute("settings")
  def g = context.generalSettingsDao.findAll.head
}
