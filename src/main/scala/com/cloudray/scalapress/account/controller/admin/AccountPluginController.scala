package com.cloudray.scalapress.account.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator
import com.cloudray.scalapress.account.{AccountPluginDao, AccountPlugin}
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@Autowired
@RequestMapping(Array("backoffice/plugin/account"))
class AccountPluginController(val context: ScalapressContext,
                              val markupDao: MarkupDao,
                              val accountPluginDao: AccountPluginDao) extends MarkupPopulator {

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
  def edit(req: HttpServletRequest) = "admin/account/plugin.vm"

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
  def save(req: HttpServletRequest, @ModelAttribute("plugin") plugin: AccountPlugin) = {
    accountPluginDao.save(plugin)
    edit(req)
  }

  @ModelAttribute("plugin")
  def plugin = accountPluginDao.get
}
