package com.liferay.scalapress.controller.admin.settings

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/settings/installation"))
class InstallationSettingsEditController {

    @Autowired var context: ScalapressContext = _

    @RequestMapping(produces = Array("text/html"))
    def edit(request: HttpServletRequest) = "admin/settings/installation.vm"

    @ModelAttribute def req(request: HttpServletRequest) = request
    @ModelAttribute def assetStore = context.assetStore
}
