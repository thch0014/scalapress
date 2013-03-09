package com.liferay.scalapress.controller.admin.settings.general

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.dao.settings.{SiteDao, GeneralSettingsDao}
import com.liferay.scalapress.domain.setup.{GeneralSettings, Site}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/settings/site"))
class GeneralSettingsEditController {

    @Autowired var siteDao: SiteDao = _
    @Autowired var generalSettingsDao: GeneralSettingsDao = _
    @Autowired var context: ScalapressContext = _

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.GET), produces = Array("text/html"))
    def edit(@ModelAttribute gsettings: GeneralSettings, @ModelAttribute site: Site) = "admin/settings/site.vm"

    @ResponseBody
    @RequestMapping(method = Array(RequestMethod.POST), produces = Array("text/html"))
    def save(@ModelAttribute gsettings: GeneralSettings, @ModelAttribute site: Site) = {
        generalSettingsDao.save(gsettings)
        siteDao.save(site)
        edit(gsettings, site)
    }

    @ModelAttribute def g = generalSettingsDao.findAll().head
    @ModelAttribute def s = siteDao.findAll().head
}
