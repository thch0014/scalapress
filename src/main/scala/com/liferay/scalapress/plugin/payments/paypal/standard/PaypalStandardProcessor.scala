package com.liferay.scalapress.plugin.payments.paypal.standard

import java.util.UUID
import com.liferay.scalapress.plugin.ecommerce.domain.Payment
import com.liferay.scalapress.Logging
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.params.BasicHttpParams
import org.apache.http.util.EntityUtils
import com.liferay.scalapress.plugin.payments.{RequiresPayment, FormPaymentProcessor}

/** @author Stephen Samuel */
class PaypalStandardProcessor(plugin: PaypalStandardPlugin)
  extends FormPaymentProcessor with Logging {

    private val PaymentTypeId = "PaypalStandard"
    private val Sandbox = "https://www.sandbox.paypal.com/cgi-bin/webscr"
    private val Production = "https://www.paypal.com/cgi-bin/webscr"
    private val Callback = "plugin/payment/paypal/standard/callback"

    def paymentUrl: String = if (plugin.production) Production else Sandbox

    def params(domain: String, basket: RequiresPayment): Map[String, String] = {

        val url = "http://" + domain.toLowerCase.replace("http://", "")
        val params = scala.collection.mutable.Map[String, String]()

        params += ("cmd" -> "_xclick")
        params += ("currency_code" -> "GBP")

        // Your PayPal ID or an email address associated with your PayPal account. Email addresses must be confirmed.
        params += ("business" -> plugin.accountEmail)

        params += ("cancel_return" -> (url + basket.failureUrl))
        params += ("return" -> (url + basket.successUrl))

        //The URL to which PayPal posts information about the payment, in the form of Instant Payment Notification messages.
        params += ("notify_url" -> (url + Callback))

        params += ("item_name" -> ("Order at " + domain))
        params += ("quantity" -> "1")

        params += ("invoice" -> UUID.randomUUID().toString)
        params += ("custom" -> basket.uniqueIdent)

        params += ("no_note" -> "1")

        // The price or amount of the product, service, or contribution, not including shipping, handling, or tax.
        // If this variable is omitted from Buy Now or Donate buttons, buyers enter their own amount at the time of payment.
        val amount = "%.2f".format(basket.total / 100.0)
        params += ("amount" -> amount)

        if (basket.accountEmail != null)
            params += ("email" -> basket.accountEmail)

        params.toMap
    }

    private def _createPayment(params: Map[String, String]): Payment = {
        logger.debug("Creating payment [{}]", params)

        val payment = new Payment
        payment.transactionId = params("txn_id")
        payment.amount = (params("payment_gross").toDouble * 100).toInt
        payment.paymentType = "paypal-standard"
        payment.date = System.currentTimeMillis()
        payment
    }

    def callback(params: Map[String, String]): Option[Payment] = {
        _isPaymentCompleted(params) match {
            case false =>
                logger.debug("IPN callback not valid")
                None
            case true =>
                logger.debug("IPN callback is valid")
                val payment = _createPayment(params)
                Option(payment)
        }
    }

    private def _isGenuineCallback(params: Map[String, String]): Boolean = {

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

    private def _isPaymentCompleted(params: Map[String, String]): Boolean = {
        // check the callback was genuine from paypal
        _isGenuineCallback(params) &&
          // determines whether the transaction is complete
          "COMPLETED".equalsIgnoreCase(params("payment_status")) &&
          // Check email address to make sure that this is not a spoof
          plugin.accountEmail.equals(params("receiver_email"))
    }
}
