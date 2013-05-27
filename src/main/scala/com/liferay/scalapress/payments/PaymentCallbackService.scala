package com.liferay.scalapress.payments

import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import com.liferay.scalapress.{Logging, Callback, ScalapressContext}
import com.liferay.scalapress.util.ComponentClassScanner

/** @author Stephen Samuel */
@Component
class PaymentCallbackService extends Logging {

    @Autowired var context: ScalapressContext = _

    def callbacks(req: HttpServletRequest) {
        val params: java.util.Map[String, Array[String]] = req.getParameterMap.asInstanceOf[java.util.Map[String, Array[String]]]
        val scala = params.asScala
        val singleString = scala.map(entry => (entry._1, entry._2(0))).toMap
        callbacks(singleString)
    }

    def callbacks(params: Map[String, String]) {
        context.paymentPluginDao.enabled.foreach(plugin => {
            plugin.processor.callback(params) match {
                case Some(result) => _processResult(result)
                case _ =>
            }
        })
    }

    def _processResult(result: CallbackResult) {
        context.transactionDao.save(result.tx)
        val callbackClass = ComponentClassScanner.callbacks
          .find(_.getAnnotation(classOf[Callback]).value.toLowerCase == result.callback.toLowerCase)
          .map(_.asInstanceOf[Class[PaymentCallback]])
        callbackClass match {
            case None => logger.warn("Callback specified type [{}] but could not resolve class", result.callback)
            case Some(klass) =>
                Option(context.bean(klass)) match {
                    case None => logger.warn("No bean found [{}]", klass)
                    case Some(callback) => callback.callback(result.tx, result.uniqueId)
                }
        }
    }
}