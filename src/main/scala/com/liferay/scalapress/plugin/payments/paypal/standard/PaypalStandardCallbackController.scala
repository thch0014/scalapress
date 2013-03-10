package com.liferay.scalapress.plugin.payments.paypal.standard

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{Logging, ScalapressContext}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("plugin/payment/paypal/standard/callback"))
class PaypalStandardCallbackController extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var dao: PaypalStandardPluginDao = _

    @ResponseBody
    @RequestMapping
    def calback(req: HttpServletRequest, resp: HttpServletResponse) {
        val params = req.getParameterMap.asScala.asInstanceOf[Map[String, String]]
        logger.debug("Paypal callback [{}]", params)
        val paypalPlugin = dao.get
        paypalPlugin.processor.callback(params) match {
            case None =>
                resp.setStatus(500)
            case Some(payment) =>
                context.paymentDao.save(payment)
                resp.setStatus(204)
        }
    }
}
