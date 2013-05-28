package com.cloudray.scalapress.search.controller.admin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{RequestMethod, ModelAttribute, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest
import com.cloudray.scalapress.search.{SearchPluginDao, SearchPlugin}
import com.cloudray.scalapress.theme.MarkupDao
import com.cloudray.scalapress.obj.controller.admin.MarkupPopulator

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/search"))
class SearchPluginController extends MarkupPopulator {

    @Autowired var context: ScalapressContext = _
    @Autowired var markupDao: MarkupDao = _
    @Autowired var searchPluginDao: SearchPluginDao = _

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
    @ModelAttribute("plugin") def plugin = searchPluginDao.findAll().head
}