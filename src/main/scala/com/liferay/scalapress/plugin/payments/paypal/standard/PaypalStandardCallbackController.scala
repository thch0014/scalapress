package com.liferay.scalapress.plugin.payments.paypal.standard

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{ScalapressRequest, ScalapressContext}
import com.liferay.scalapress.dao.settings.InstallationDao
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("paypal/standard/callback"))
class PaypalStandardCallbackController {

    @Autowired var context: ScalapressContext = _
    @Autowired var dao: PaypalStandardPluginDao = _
    @Autowired var installationDao: InstallationDao = _

    @ResponseBody
    @RequestMapping
    def calback(req: HttpServletRequest, resp: HttpServletResponse) {
        val sreq = ScalapressRequest(req, context)


        resp.setStatus(204)
    }
}
