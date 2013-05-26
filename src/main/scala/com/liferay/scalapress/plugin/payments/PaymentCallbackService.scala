package com.liferay.scalapress.plugin.payments

import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import com.liferay.scalapress.ScalapressContext

/** @author Stephen Samuel */
@Component
class PaymentCallbackService {

    @Autowired var context: ScalapressContext = _
    @Autowired var txDao: TransactionDao = _
    @Autowired var paymentPluginDao: PaymentPluginDao = _

    def callbacks(req: HttpServletRequest) {
        val params: java.util.Map[String, Array[String]] = req.getParameterMap.asInstanceOf[java.util.Map[String, Array[String]]]
        val scala = params.asScala
        val singleString = scala.map(entry => (entry._1, entry._2(0))).toMap
        callbacks(singleString)
    }

    def callbacks(params: Map[String, String]) {
        paymentPluginDao.enabled.foreach(plugin => {
            plugin.processor.callback(params) match {
                case Some(result) => _processResult(result)
                case None =>
            }
        })
    }

    def _processResult(result: CallbackResult) {
        txDao.save(result.tx)
        context.bean(result.callbackClass).callback(result.tx, result.uniqueId)
    }
}
