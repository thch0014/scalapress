package com.cloudray.scalapress.plugin.payments.sagepayform

import org.apache.commons.codec.binary.Base64
import com.cloudray.scalapress.Logging
import com.cloudray.scalapress.plugin.ecommerce.domain.Basket
import scala.collection.JavaConverters._
import com.cloudray.scalapress.payments.{Transaction, Purchase, CallbackResult, PaymentProcessor}

/** @author Stephen Samuel */
class SagepayFormProcessor(plugin: SagepayFormPlugin) extends PaymentProcessor with Logging {

  val PaymentTypePayment = "PAYMENT"
  val PaymentTypeDeferred = "DEFERRED"
  val VPSProtocol: String = "2.23"

  val LiveUrl = "https://live.sagepay.com/gateway/service/vspform-register.vsp"
  val TestUrl = "https://test.sagepay.com/gateway/service/vspform-register.vsp"

  def paymentProcessorName = "SagePayForm"

  def xor(in: Array[Byte], key: String) = {
    val result = new Array[Byte](in.length)
    for ( k <- 0 until in.length ) {
      val b = in(k) ^ key.charAt(k % key.length)
      result(k) = b.toByte
    }
    result
  }

  def decryptParams(base64: String): Map[String, String] = {
    val encrypted = Base64.decodeBase64(base64.getBytes)
    val x = xor(encrypted, plugin.sagePayEncryptionPassword)
    val decrypted = new String(x)
    _splitResponseString(decrypted)
  }

  def _splitResponseString(input: String): Map[String, String] = {
    input.split("&").map(_.split("=")).filter(_.size == 2).map(a => (a(0), a(1))).toMap
  }

  def encryptParams(params: Map[String, String]): String = {
    val data = params.map(e => e._1 + "=" + e._2).mkString("&")
    val x = xor(data.getBytes, plugin.sagePayEncryptionPassword)
    val base64 = Base64.encodeBase64(x)
    new String(base64)
  }

  // returns the four params needed by sagepay
  def params(domain: String, purchase: Purchase): Map[String, String] = {
    val crypt = encryptParams(_cryptParams(purchase, domain))
    Map(
      "VPSProtocol" -> VPSProtocol,
      "TxType" -> PaymentTypePayment,
      "Vendor" -> plugin.sagePayVendorName,
      "Crypt" -> crypt
    )
  }

  def _createTx(params: Map[String, String]) = {

    val status = params.get("Status").getOrElse("NoStatus")
    val transactionId = params.get("VPSTxId").orNull
    val authCode = params.get("TxAuthNo").orNull
    val cardType = params.get("CardType").orNull
    val fraudHint = params.get("FraudResponse").orNull
    val securityCheck = params.get("AVSCV2").orNull
    val details = params.get("StatusDetail").orNull
    val amount: Int = try {
      params.get("Amount").map(_.toDouble).map(_ * 100).map(_.toInt).getOrElse(0)
    } catch {
      case e: Exception => 0
    }

    val tx = Transaction(transactionId, paymentProcessorName, amount, status)
    tx.transactionId = transactionId
    tx.authCode = authCode
    tx.cardType = cardType
    tx.securityCheck = securityCheck
    tx.fraudHint = fraudHint
    tx.details = details
    tx
  }

  def sageBasketString(basket: Basket) = {

    val count = basket.lines.size + 1

    val sb = new StringBuilder
    sb.append(count)

    for ( line <- basket.lines.asScala ) {
      sb.append(":")
      sb.append(line.obj.name.replaceAll("[=:&$*]", ""))
      sb.append(":")
      sb.append(line.qty)
      sb.append(":")
      sb.append(line.obj.price / 100.0)
      sb.append(":")
      sb.append(line.obj.vat / 100.0)
      sb.append(":")
      sb.append(line.obj.sellPriceInc / 100.0)
      sb.append(":")
      sb.append(line.total / 100.0)
    }

    sb.append(":")
    sb.append(Option(basket.deliveryOption.name).getOrElse("Delivery option has no name").replaceAll("[&:=$*]", ""))
    sb.append(":1:")
    sb.append(basket.deliveryOption.charge / 100.0)
    sb.append(":")
    sb.append(basket.deliveryOption.chargeVat / 100.0)
    sb.append(":")
    sb.append(basket.deliveryOption.chargeIncVat / 100.0)
    sb.append(":")
    sb.append(basket.deliveryOption.chargeIncVat / 100.0)


    sb.toString()
  }

  def callback(params: Map[String, String]): Option[CallbackResult] = {

    val crypt = params("crypt")
    val p = decryptParams(crypt)
    logger.info("Sagepay params decrypted {}", p)

    val status = p.get("Status").getOrElse("NoStatus")

    status.toLowerCase match {
      case "ok" =>
        val sageTxId = p.get("VPSTxId").getOrElse("NoId")
        _isExistingTransaction(sageTxId) match {
          case true => None
          case false =>

            val tx = _createTx(p)
            logger.info("Created transaction [{}]", tx)

            val sessionId = _sessionId(p)
            Some(CallbackResult(tx, sessionId))
        }
      case _ =>
        logger.info("Invalid status [{}]", status)
        None
    }
  }

  def _sessionId(params: Map[String, String]) = params("VendorTxCode")
  def _isExistingTransaction(sageTxId: String) = false

  // returns the unencrpyted params used in the crypt field
  def _cryptParams(purchase: Purchase, domain: String): Map[String, String] = {

    val params = new scala.collection.mutable.HashMap[String, String]

    // Max 40 characters
    // This should be your own reference code to the transaction. Your
    // site should provide a completely unique VendorTxCode for each transaction.
    params.put("VendorTxCode", purchase.callbackInfo)
    Option(plugin.sagePayVendorEmail).foreach(email => {
      params.put("VendorEmail", plugin.sagePayVendorEmail)
    })

    val amount = "%1.2f".format(purchase.total / 100.0)

    params.put("Currency", "GBP")

    // Amount for the Transaction containing minor  digits formatted to 2 decimal places where appropriate
    params.put("Amount", amount)
    params.put("CustomerName", purchase.accountName)
    params.put("CustomerEmail", purchase.accountEmail)
    params.put("Description", purchase.paymentDescription)

    params.put("SuccessURL", purchase.successUrl)
    params.put("FailureURL", purchase.failureUrl)

    purchase.deliveryAddress.foreach(address => {

      val name = Option(address.name).getOrElse("")
      val fn = name.split(" ").last
      val ln = name.split(" ").head

      params.put("DeliveryFirstnames", fn)
      params.put("DeliverySurname", ln)
      params.put("DeliveryAddress1", address.address1)
      params.put("DeliveryCity", address.town)
      params.put("DeliveryPostCode", address.postcode)
      params.put("DeliveryCountry", "GB")
    })

    purchase.billingAddress.foreach(address => {

      val name = Option(address.name).getOrElse("")
      val fn = name.split(" ").last
      val ln = name.split(" ").head

      params.put("BillingSurname", fn)
      params.put("BillingFirstnames", ln)
      params.put("BillingAddress1", address.address1)
      params.put("BillingCity", address.town)
      params.put("BillingPostCode", address.postcode)
      params.put("BillingCountry", "GB")
    })

    purchase match {
      case basket: Basket => params.put("Basket", sageBasketString(basket))
      case _ =>
    }

    logger.debug("Params [{}]", params)
    params.toMap
  }

  def paymentUrl: String = LiveUrl

}
