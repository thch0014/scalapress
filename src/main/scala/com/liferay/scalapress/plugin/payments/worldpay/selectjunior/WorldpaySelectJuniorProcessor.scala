package com.liferay.scalapress.plugin.payments.worldpay.selectjunior

import com.liferay.scalapress.plugin.payments.{CallbackResult, PaymentProcessor, Transaction, Purchase}
import com.liferay.scalapress.Logging

/** @author Stephen Samuel */
class WorldpaySelectJuniorProcessor(plugin: WorldpaySelectJuniorPlugin) extends PaymentProcessor with Logging {

    val URL = "https://select.worldpay.com/wcc/purchase"
    val NAME: String = "WorldpaySelectJunior"

    def paymentUrl: String = URL
    def paymentProcessorName: String = NAME

    def params(domain: String, purchase: Purchase): Map[String, String] = {

        val params = scala.collection.mutable.Map[String, String]()

        params.put("instId", plugin.installationId)
        params.put("accId1", plugin.accountId)
        params.put("cartId", purchase.callbackInfo)
        params.put("MC_callback", "todo")

        // 100: random 101: failure 102: success 0: production
        val mode = if (plugin.live) "0" else "102"
        params.put("testMode", mode)

        params.put("currency", "GBP")
        params.put("country", "GB")

        params.put("name", purchase.accountName)
        params.put("email", purchase.accountEmail)
        //   params.put("address", billingAddress.getAddress("&#10"))
        //  params.put("postcode", billingAddress.getPostcode())

        params.put("authMode", plugin.authMode)

        val amount = "%1.2f".format(purchase.total / 100.0)
        params.put("amount", amount)
        params.put("desc", purchase.paymentDescription)
        params.toMap
    }

    def createTransaction(params: Map[String, String]): Option[Transaction] = {

        val transactionId = params.get("transId")
        val transactionStatus = params.get("transStatus")
        val cardType = params.get("cardType")
        val amount = params.get("authAmount").getOrElse("").replace("&#163;", "")
        val name = params.get("name")
        val rawAuthCode = params.get("rawAuthCode")
        val ipAddress = params.get("ipAddress")
        val callbackPassword = params.get("callbackPW")

        transactionStatus match {
            case Some(status) if status == "Y" =>

                if (callbackPassword.orNull == plugin.callbackPassword) {

                    val tx = Transaction(transactionId.getOrElse("unknown"), NAME, (amount.toDouble * 100).toInt)
                    tx.status = transactionStatus.orNull
                    tx.details = cardType.getOrElse("") + " " + name.getOrElse("")
                    tx.authCode = rawAuthCode.orNull
                    tx.ipAddress = ipAddress.orNull
                    logger.debug("Created transaction [{}]", tx)
                    Some(tx)

                } else {
                    logger.warn("Callback password was invalid [{}]", plugin.callbackPassword)
                    None
                }

            case _ => None
        }

    }

    def callback(params: Map[String, String]): Option[CallbackResult] = None

}

