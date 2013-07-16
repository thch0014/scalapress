package com.cloudray.scalapress.settings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import com.cloudray.scalapress.settings.{Installation, GeneralSettings, GeneralSettingsDao}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/settings/general"))
class GeneralSettingsController {

  @Autowired var generalSettingsDao: GeneralSettingsDao = _
  @Autowired var context: ScalapressContext = _

  @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
  def edit(@ModelAttribute("settings") gsettings: GeneralSettings,
           @ModelAttribute site: Installation) = "admin/settings/general.vm"

  @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
  def save(@ModelAttribute("settings") gsettings: GeneralSettings, @ModelAttribute site: Installation) = {
    generalSettingsDao.save(gsettings)
    edit(gsettings, site)
  }

  @ModelAttribute("settings") def g = generalSettingsDao.findAll().head
}
