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
@RequestMapping(Array("payment/callback/listing"))
class ListingPaymentCallbackController extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var dao: PaymentPluginDao = _
    @Autowired var listingProcessService: ListingProcessService = _

    def _params(req: HttpServletRequest): Map[String, String] = req
      .getParameterMap
      .asScala
      .map(arg => (arg._1.asInstanceOf[String], arg._2.asInstanceOf[Array[String]].head))
      .toMap

    @ResponseBody
    @RequestMapping
    def calback(req: HttpServletRequest, resp: HttpServletResponse) {

        val params = _params(req)

        logger.info("Paypal Callback...")
        params.foreach(entry => logger.info("{}={}", entry._1, entry._2))
        logger.info("... req={}", req)

        dao.enabled.foreach(plugin => {
            plugin.processor.callback(params) match {
                case None =>
                case Some(tx) =>
                    logger.debug("Persisting transaction [{}]", tx)
                    context.transactionDao.save(tx)
                    Option(context.listingProcessDao.find(params.get("custom").getOrElse("0"))) match {
                        case None => logger.warn("Could not find listing process session for callback")
                        case (Some(process)) =>
                            logger.info("Processing process to listing [{}]", process)
                            listingProcessService.process(tx, process, req)
                    }
            }
        })

        resp.setStatus(200)
    }
}
