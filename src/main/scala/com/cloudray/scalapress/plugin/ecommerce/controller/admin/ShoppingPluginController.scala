package com.cloudray.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.plugin.ecommerce.{ShoppingPlugin, ShoppingPluginDao}
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator
import com.cloudray.scalapress.util.EnumPopulator
import com.cloudray.scalapress.item.StockMethod

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/shopping"))
@Autowired
class ShoppingPluginController(val context: ScalapressContext,
                               val markupDao: MarkupDao,
                               val shoppingPluginDao: ShoppingPluginDao) extends MarkupPopulator with EnumPopulator {

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
  def edit(req: HttpServletRequest,
           @ModelAttribute("plugin") plugin: ShoppingPlugin) = "admin/plugin/shopping/plugin.vm"

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
  def save(req: HttpServletRequest, @ModelAttribute("plugin") plugin: ShoppingPlugin) = {
    shoppingPluginDao.save(plugin)
    edit(req, plugin)
  }

  @ModelAttribute("plugin") def plugin = shoppingPluginDao.get
  @ModelAttribute("stockMethods") def stockMethods = populate(StockMethod.values)
}
