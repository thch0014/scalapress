package com.liferay.scalapress.plugin.payments.sagepayform

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/sagepayform"))
class SagepayFormPluginController {

    @Autowired var context: ScalapressContext = _
    @Autowired var dao: SagepayFormPluginDao = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(req: HttpServletRequest,
             @ModelAttribute("plugin") plugin: SagepayFormPlugin) = "admin/plugin/payment/sagepayform/plugin.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(req: HttpServletRequest, @ModelAttribute("plugin") plugin: SagepayFormPlugin) = {
        dao.save(plugin)
        edit(req, plugin)
    }

    @ModelAttribute("plugin") def plugin = dao.get
}
