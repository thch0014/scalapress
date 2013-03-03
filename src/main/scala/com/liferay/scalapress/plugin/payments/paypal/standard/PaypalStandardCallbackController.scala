package com.liferay.scalapress.plugin.payments.paypal.standard

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.dao.settings.InstallationDao
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("plugin/payment/paypal/standard/callback"))
class PaypalStandardCallbackController {

    @Autowired var context: ScalapressContext = _
    @Autowired var dao: PaypalStandardPluginDao = _
    @Autowired var installationDao: InstallationDao = _

    @ResponseBody
    @RequestMapping
    def calback(req: HttpServletRequest, resp: HttpServletResponse) {
        val params = req.getParameterMap.asScala.asInstanceOf[Map[String, String]]
        PaypalStandardService.callback(params, dao.get) match {
            case None =>
            case Some(payment) => context.paymentDao.save(payment)
        }
        resp.setStatus(204)
    }
}
