package com.cloudray.scalapress.plugin.account.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.account.{AccountPlugin, AccountPluginDao}
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.obj.controller.admin.MarkupPopulator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/account"))
class AccountPluginController extends MarkupPopulator {

  @Autowired var context: ScalapressContext = _
  @Autowired var markupDao: MarkupDao = _
  @Autowired var accountPluginDao: AccountPluginDao = _

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
  def edit(req: HttpServletRequest) = "admin/plugin/account/plugin.vm"

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
  def save(req: HttpServletRequest, @ModelAttribute("plugin") plugin: AccountPlugin) = {
    accountPluginDao.save(plugin)
    edit(req)
  }

  @ModelAttribute("plugin") def plugin = accountPluginDao.get
}
