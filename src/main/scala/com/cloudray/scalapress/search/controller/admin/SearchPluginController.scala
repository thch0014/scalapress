package com.cloudray.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.search.{SearchPluginDao, SearchPlugin}
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.item.controller.admin.MarkupPopulator
import com.cloudray.scalapress.framework.ScalapressContext

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/searchsettings"))
@Autowired
class SearchPluginController(val markupDao: MarkupDao,
                             val searchPluginDao: SearchPluginDao,
                             val context: ScalapressContext) extends MarkupPopulator {

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.GET))
  def edit(req: HttpServletRequest,
           @ModelAttribute("plugin") searchPlugin: SearchPlugin) = "admin/plugin/search/plugin.vm"

  @RequestMapping(produces = Array("text/html"), method = Array(RequestMethod.POST))
  def save(req: HttpServletRequest, @ModelAttribute("plugin") searchPlugin: SearchPlugin) = {
    searchPluginDao.save(searchPlugin)
    edit(req, searchPlugin)
  }

  @ModelAttribute def req(request: HttpServletRequest) = request
  @ModelAttribute def assetStore = context.assetStore
  @ModelAttribute("plugin") def plugin = searchPluginDao.findAll.head
}
