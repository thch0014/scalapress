package com.cloudray.scalapress.plugin.security.simplepass

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import scala.Array
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/security/simplepass"))
class SimplePassPluginController(simplePassPluginDao: SimplePassPluginDao) {

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
  def edit(req: HttpServletRequest,
           @ModelAttribute("plugin") plugin: SimplePassPlugin) = "admin/plugin/security/simplepass/plugin.vm"

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
  def save(req: HttpServletRequest, @ModelAttribute("plugin") plugin: SimplePassPlugin) = {
    simplePassPluginDao.save(plugin)
    edit(req, plugin)
  }

  @ModelAttribute("plugin") def plugin = simplePassPluginDao.get
}
