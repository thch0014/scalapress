package com.liferay.scalapress.settings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.settings.{Installation, GeneralSettings, GeneralSettingsDao, InstallationDao}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/settings/site"))
class GeneralSettingsEditController {

    @Autowired var siteDao: InstallationDao = _
    @Autowired var generalSettingsDao: GeneralSettingsDao = _
    @Autowired var context: ScalapressContext = _

    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute gsettings: GeneralSettings, @ModelAttribute site: Installation) = "admin/settings/site.vm"

    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute gsettings: GeneralSettings, @ModelAttribute site: Installation) = {
        generalSettingsDao.save(gsettings)
        siteDao.save(site)
        edit(gsettings, site)
    }

    @ModelAttribute def g = generalSettingsDao.findAll().head
    @ModelAttribute def s = siteDao.findAll().head
}
