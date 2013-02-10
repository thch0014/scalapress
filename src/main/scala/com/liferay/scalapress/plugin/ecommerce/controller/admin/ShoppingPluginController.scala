package com.liferay.scalapress.plugin.ecommerce.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.dao.MarkupDao
import scala.Array
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.plugin.ecommerce.{ShoppingPlugin, ShoppingPluginDao}
import com.liferay.scalapress.controller.admin.obj.MarkupPopulator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/shopping"))
class ShoppingPluginController extends MarkupPopulator {

    @Autowired var context: ScalapressContext = _
    @Autowired var markupDao: MarkupDao = _
    @Autowired var shoppingPluginDao: ShoppingPluginDao = _

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
    def edit(req: HttpServletRequest,
             @ModelAttribute("plugin") plugin: ShoppingPlugin) = "admin/plugin/shopping/plugin.vm"

    @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
    def save(req: HttpServletRequest, @ModelAttribute("plugin") plugin: ShoppingPlugin) = {
        shoppingPluginDao.save(plugin)
        edit(req, plugin)
    }

    @ModelAttribute("plugin") def plugin = shoppingPluginDao.get
}
