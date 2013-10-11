package com.cloudray.scalapress.plugin.payments.paypal.standard

import com.cloudray.scalapress.Logging
import com.cloudray.scalapress.payments.{Transaction, Purchase, CallbackResult, PaymentProcessor}
import org.apache.http.params.BasicHttpParams
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils

/** @author Stephen Samuel */
class PaypalStandardProcessor(plugin: PaypalStandardPlugin) extends PaymentProcessor with Logging {

  private val Sandbox = "https://www.sandbox.paypal.com/cgi-bin/webscr"
  private val Production = "https://www.paypal.com/cgi-bin/webscr"

  def paymentProcessorName = "PaypalStandard"
  def paymentUrl: String = if (plugin.production) Production else Sandbox

  def params(domain: String, purchase: Purchase): Map[String, String] = {

    val params = scala.collection.mutable.Map[String, String]()

    params += ("cmd" -> "_xclick")
    params += ("currency_code" -> "GBP")

    // Your PayPal ID or an email address associated with your PayPal account. Email addresses must be confirmed.
    params += ("business" -> plugin.accountEmail)

    params += ("cancel_return" -> purchase.failureUrl)
    params += ("return" -> purchase.successUrl)

    //The URL to which PayPal posts information about the payment, in the form of Instant Transaction Notification messages.
    // todo setup paypal callback url
    //        params += ("notify_url" -> (url + payable.callbackUrl + "?paymentPluginClass=" + classOf[PaypalStandardPlugin]
    //        .getName))

    params += ("item_name" -> purchase.paymentDescription)
    params += ("quantity" -> "1")

    params += ("invoice" -> purchase.uniqueIdent)
    params += ("custom" -> purchase.callbackInfo)

    params += ("no_note" -> "1")

    // The price or amount of the product, service, or contribution, not including shipping, handling, or tax.
    // If this variable is omitted from Buy Now or Donate buttons, buyers enter their own amount at the time of payment.
    val amount = "%.2f".format(purchase.total / 100.0)
    params += ("amount" -> amount)

    if (purchase.accountEmail != null)
      params += ("email" -> purchase.accountEmail)

    params.toMap
  }

  def _createTx(params: Map[String, String]): Option[Transaction] = {
    logger.debug("Creating Tx from Params [{}]", params)

    params.get("txn_id") match {
      case None => None

      case Some(txid) =>
        val amount = (params.get("mc_gross").filter(_.trim.length > 0).getOrElse("0").toDouble * 100).toInt

        val tx = Transaction(txid, paymentProcessorName, amount)
        tx.status = params("payment_status")
        tx.currency = params("mc_currency")
        tx.transactionType = params("payment_type")
        tx.payerStatus = params("payer_status")
        tx.paymentProcessor = paymentProcessorName
        tx.payee = (params.get("first_name").getOrElse("") + " " + params.get("last_name").getOrElse("")).trim
        tx.payeeEmail = params.get("payer_email").orNull

        Some(tx)
    }
  }

  def callback(params: Map[String, String]): Option[CallbackResult] = {
    //    _isPaymentCompleted(params) match {
    //        case false =>
    //            logger.info("IPN callback not valid")
    //            None
    //       case true =>
    //            logger.info("IPN callback is valid")
    _createTx(params).map(tx => CallbackResult(tx, _custom(params)))
  }

  def _custom(params: Map[String, String]): String = params("custom")

  def _isGenuineCallback(params: Map[String, String]): Boolean = {

    val queryParams = new BasicHttpParams()
    queryParams.setParameter("cmd", "_notify-validate")
    for ( (key, value) <- params ) {
      queryParams.setParameter(key, value)
    }

    val get = new HttpGet("https://www.paypal.com/cgi-bin/webscr")
    get.setParams(queryParams)

    val client = new DefaultHttpClient
    val resp = client.execute(get)

    val string = EntityUtils.toString(resp.getEntity)
    "VERIFIED".equalsIgnoreCase(string)
  }

  def _isPaymentCompleted(params: Map[String, String]): Boolean = {
    // check the callback was genuine from paypal
    _isGenuineCallback(params) &&
      // determines whether the transaction is complete
      "COMPLETED".equalsIgnoreCase(params("payment_status")) &&
      // Check email address to make sure that this is not a spoof
      plugin.accountEmail.equals(params("receiver_email"))
  }
}
