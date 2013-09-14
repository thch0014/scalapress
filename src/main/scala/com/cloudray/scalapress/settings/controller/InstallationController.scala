package com.cloudray.scalapress.settings.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.settings.Installation

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/settings/installation"))
class InstallationController {

  @Autowired var context: ScalapressContext = _

  @RequestMapping(method = Array(RequestMethod.GET))
  def edit(request: HttpServletRequest) = "admin/settings/installation.vm"

  @RequestMapping(method = Array(RequestMethod.POST))
  def save(request: HttpServletRequest, @ModelAttribute installation: Installation) = {
    context.installationDao.save(installation)
    "admin/settings/installation.vm"
  }

  @ModelAttribute("sreq") def req(request: HttpServletRequest) = request
  @ModelAttribute def assetStore = context.assetStore
  @ModelAttribute def installation = context.installationDao.get
}
