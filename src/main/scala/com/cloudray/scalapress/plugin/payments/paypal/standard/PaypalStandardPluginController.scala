package com.cloudray.scalapress.plugin.payments.paypal.standard

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMethod, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.ScalapressContext
import javax.servlet.http.HttpServletRequest

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("backoffice/plugin/payment/paypal/standard"))
class PaypalStandardPluginController {

    @Autowired var context: ScalapressContext = _
    @Autowired var dao: PaypalStandardPluginDao = _

    @RequestMapping(method = Array(RequestMethod.GET))
    def edit(req: HttpServletRequest,
             @ModelAttribute("plugin") plugin: PaypalStandardPlugin) = "admin/plugin/payment/paypal/standard/plugin.vm"

    @RequestMapping(method = Array(RequestMethod.POST))
    def save(req: HttpServletRequest, @ModelAttribute("plugin") plugin: PaypalStandardPlugin) = {
        dao.save(plugin)
        edit(req, plugin)
    }

    @ModelAttribute("plugin") def plugin = dao.get
}
