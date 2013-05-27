package com.liferay.scalapress.payments

import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import com.liferay.scalapress.{Tag, ScalapressContext}
import com.liferay.scalapress.util.ComponentClassScanner

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
        val klass = ComponentClassScanner.callbacks
          .find(_.getAnnotation(classOf[Tag]).value.toLowerCase == result.callback.toLowerCase)
          .map(_.asInstanceOf[Class[PaymentCallback]]).get
        context.bean(klass).callback(result.tx, result.uniqueId)
    }
}
