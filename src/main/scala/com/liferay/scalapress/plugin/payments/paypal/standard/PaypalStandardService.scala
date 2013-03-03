package com.liferay.scalapress.plugin.payments.paypal.standard

import java.util.UUID
import com.liferay.scalapress.domain.setup.Installation
import com.liferay.scalapress.plugin.ecommerce.domain.{Payment, Basket}
import com.liferay.scalapress.Logging
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.params.BasicHttpParams
import org.apache.http.util.EntityUtils

/** @author Stephen Samuel */
class PaypalStandardService extends Logging {

    val PaymentTypeId = "PaypalStandard"
    val Sandbox = "https://www.sandbox.paypal.com/cgi-bin/webscr"
    val Production = "https://www.paypal.com/cgi-bin/webscr"

    def paymentUrl: String = Production

    def params(plugin: PaypalStandardPlugin, installation: Installation, basket: Basket): Map[String, String] = {

        val params = scala.collection.mutable.Map[String, String]()

        params += ("cmd" -> "_xclick")
        params += ("currency_code" -> "GBP")

        // Your PayPal ID or an email address associated with your PayPal account. Email addresses must be confirmed.
        params += ("business" -> plugin.accountEmail)

        params += ("cancel_return" -> "/checkout/payment/failure")
        params += ("return" -> "/checkout/payment/success")

        //The URL to which PayPal posts information about the payment, in the form of Instant Payment Notification messages.
        params += ("notify_url" -> "qweee")

        params += ("item_name" -> ("Order at " + installation.domain))
        params += ("quantity" -> "1")

        params += ("invoice" -> UUID.randomUUID().toString)
        params += ("custom" -> basket.sessionId)

        params += ("no_note" -> "1")

        // The price or amount of the product, service, or contribution, not including shipping, handling, or tax.
        // If this variable is omitted from Buy Now or Donate buttons, buyers enter their own amount at the time of payment.
        params += ("amount" -> basket.total.toString)

        if (basket.accountEmail != null)
            params += ("email" -> basket.accountEmail)

        params.toMap
    }

    def createPayment(params: Map[String, String]): Payment = {
        logger.debug("Creating payment [{}]", params)

        val payment = new Payment
        payment.transactionId = params("txn_id")
        payment.amount = (params("payment_gross").toDouble * 100).toInt
        payment.paymentType = "paypal-standard"
        payment.date = System.currentTimeMillis()
        payment
    }

    def callback(params: Map[String, String], plugin: PaypalStandardPlugin): Option[Payment] = {
        logger.debug("**Paypal Callback** {}", params)

        paymentCompleted(params, plugin) match {
            case false =>
                logger.debug("IPN callback not valid")
                None
            case true =>
                logger.debug("IPN callback is valid")
                val payment = createPayment(params)
                Option(payment)
        }
    }

    def genuineCallback(params: Map[String, String]): Boolean = {

        val queryParams = new BasicHttpParams()
        queryParams.setParameter("cmd", "_notify-validate")
        for ((key, value) <- params) {
            queryParams.setParameter(key, value)
        }

        val get = new HttpGet("https://www.paypal.com/cgi-bin/webscr")
        get.setParams(queryParams)

        val client = new DefaultHttpClient
        val resp = client.execute(get)

        val string = EntityUtils.toString(resp.getEntity)
        "VERIFIED".equalsIgnoreCase(string)
    }

    def paymentCompleted(params: Map[String, String], plugin: PaypalStandardPlugin): Boolean = {
        // check the callback was genuine from paypal
        genuineCallback(params) &&
          // determines whether the transaction is complete
          "COMPLETED".equalsIgnoreCase(params("payment_status")) &&
          // Check email address to make sure that this is not a spoof
          plugin.accountEmail.equals(params("receiver_email"))
    }
}
