package com.liferay.scalapress.plugin.listings.controller

import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.liferay.scalapress.{Logging, ScalapressContext}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import process.ListingEmailService
import scala.collection.JavaConverters._
import com.liferay.scalapress.plugin.payments.PaymentPluginDao
import com.liferay.scalapress.plugin.listings.ListingProcess2ObjectBuilder

/** @author Stephen Samuel */
@Controller
@RequestMapping(Array("payment/callback/listing"))
class ListingPaymentCallbackController extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var dao: PaymentPluginDao = _

    @Autowired var listingEmailService: ListingEmailService = _

    @ResponseBody
    @RequestMapping
    def calback(req: HttpServletRequest, resp: HttpServletResponse) {

        val params = req
          .getParameterMap
          .asScala
          .map(arg => (arg._1.asInstanceOf[String], arg._2.asInstanceOf[Array[String]].head))
          .toMap

        logger.info("Paypal Callback...")
        params.foreach(entry => logger.info("{}={}", entry._1, entry._2))
        logger.info("... req={}", req)

        dao.enabled.foreach(plugin => {
            plugin.processor.callback(params) match {
                case None =>
                case Some(tx) =>
                    context.transactionDao.save(tx)
                    Option(context.listingProcessDao.find(params.get("custom").getOrElse("0"))) match {
                        case None =>
                        case (Some(process)) =>
                            logger.info("Processing process to listing [{}]", process)

                            val builder = new ListingProcess2ObjectBuilder(context)
                            val obj = builder.build(process)
                            listingEmailService.send(obj, context)

                            logger.info("Process completed - removing from database")
                            context.listingProcessDao.remove(process)
                    }
            }
        })

        resp.setStatus(200)
    }
}
