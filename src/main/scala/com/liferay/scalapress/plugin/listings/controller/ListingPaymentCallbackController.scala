package com.liferay.scalapress.plugin.listings.controller

import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{Logging, ScalapressContext}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.plugin.payments.PaymentPluginDao

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("listing/payment/callback"))
class ListingPaymentCallbackController extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var dao: PaymentPluginDao = _

    @ResponseBody
    @RequestMapping
    def calback(req: HttpServletRequest, resp: HttpServletResponse) {
        val params = req.getParameterMap.asScala.asInstanceOf[Map[String, String]]
        logger.debug("Callback [{}]", params)

        dao.enabled.foreach(plugin => {
            plugin.processor.callback(params) match {
                case None =>
                    resp.setStatus(500)
                case Some(payment) =>
                    context.transactionDao.save(payment)
                    resp.setStatus(204)
            }
        })
    }
}