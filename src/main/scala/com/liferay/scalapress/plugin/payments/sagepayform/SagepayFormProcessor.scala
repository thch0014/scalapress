package com.liferay.scalapress.plugin.payments.sagepayform

import org.apache.commons.codec.binary.Base64
import com.liferay.scalapress.Logging
import com.liferay.scalapress.plugin.ecommerce.domain.{Basket, Payment}
import java.util.UUID
import com.liferay.scalapress.plugin.payments.{RequiresPayment, FormPaymentProcessor}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
class SagepayFormProcessor(plugin: SagepayFormPlugin) extends FormPaymentProcessor with Logging {

    val PaymentTypeId = "SagePayForm"
    val PaymentTypePayment = "PAYMENT"
    val PaymentTypeDeferred = "DEFERRED"
    val VPSProtocol: String = "2.23"

    val LiveUrl = "https://live.sagepay.com/gateway/service/vspform-register.vsp"
    val TestUrl = "https://test.sagepay.com/gateway/service/vspform-register.vsp"

    private def xor(in: Array[Byte], key: String) = {
        val result = new Array[Byte](in.length)
        for (k <- 0 until in.length) {
            val b = in(k) ^ key.charAt(k % key.length)
            result(k) = b.toByte
        }
        result
    }

    private def decryptParams(base64: String): Map[String, String] = {

        val encrypted = Base64.decodeBase64(base64.getBytes)
        val x = xor(encrypted, plugin.sagePayEncryptionPassword)
        val decrypted = new String(x)
        val params = decrypted.split("&").map(_.split("=")).filter(_.size == 2).map(a => (a(0), a(1))).toMap
        params
    }

    private def encryptParams(params: Map[String, String]): String = {
        val data = params.map(e => e._1 + "=" + e._2).mkString("&")
        val x = xor(data.getBytes, plugin.sagePayEncryptionPassword)
        val base64 = Base64.encodeBase64(x)
        new String(base64)
    }

    // returns the four params needed by sagepay
    def params(domain: String, requiresPayment: RequiresPayment): Map[String, String] = {

        val crypt = encryptParams(_cryptParams(requiresPayment, domain))

        val params = Map("VPSProtocol" -> VPSProtocol,
            "TxType" -> PaymentTypePayment,
            "Vendor" -> plugin.sagePayVendorName,
            "Crypt" -> crypt)

        params
    }

    // creates a payment object for the given set of response params
    private def createPayment(params: Map[String, String]) = {

        val orderId = params.get("VendorTxCode").getOrElse("0").toLong
        val transactionId = params.get("VPSTxId").orNull
        val authCode = params.get("TxAuthNo").orNull
        val amount: Int = (params.get("Amount").getOrElse("0").toDouble * 100).toInt

        val payment = new Payment
        payment.paymentType = PaymentTypeId
        payment.date = System.currentTimeMillis()
        payment.transactionId = transactionId
        payment.authCode = authCode
        payment.order = orderId
        payment.amount = amount
        payment
    }

    private def sageBasketString(basket: Basket) = {

        val count = basket.lines.size + 1

        val sb = new StringBuilder
        sb.append(count)

        for (line <- basket.lines.asScala) {
            sb.append(":")
            sb.append(line.obj.name.replaceAll("[=:&$*]", ""))
            sb.append(":")
            sb.append(line.qty)
            sb.append(":")
            sb.append(line.obj.sellPrice / 100.0)
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

    def callback(params: Map[String, String]): Option[Payment] = {
        logger.debug("Callback params")

        val crypt = params.get("crypt").toString
        val p = decryptParams(crypt)
        logger.debug("Sagepay params {}", p)

        val status = p.get("Status").getOrElse("NoStatus")

        status.toLowerCase match {
            case "ok" =>
                val sageTxId = p.get("VPSTxId").getOrElse("NoId")
                _isExistingTransaction(sageTxId) match {
                    case true => None
                    case false => Some(createPayment(p))
                }
            case _ => None
        }
    }

    private def _isExistingTransaction(sageTxId: String) = false

    // returns the unencrpyted params used in the crypt field
    private def _cryptParams(requiresPayment: RequiresPayment,
                             domain: String): Map[String, String] = {

        val params = new scala.collection.mutable.HashMap[String, String]
        params.put("VendorTxCode", UUID.randomUUID.toString)
        Option(plugin.sagePayVendorEmail).foreach(email => {
            params.put("VendorEmail", plugin.sagePayVendorEmail)
        })

        val amount = "%1.2f".format(requiresPayment.total / 100.0)

        params.put("Currency", "GBP")
        params.put("Amount", amount)
        params.put("CustomerName", requiresPayment.accountName)
        params.put("CustomerEmail", requiresPayment.accountEmail)
        params.put("Description", "Order at " + domain)

        params.put("SuccessURL", "http://" + domain + requiresPayment.successUrl)
        params.put("FailureURL", "http://" + domain + requiresPayment.failureUrl)

        val deliveryName = Option(requiresPayment.deliveryAddress.name).getOrElse("")

        val firstname = deliveryName.split(" ").last
        val lastname = deliveryName.split(" ").head

        params.put("DeliveryFirstnames", firstname)
        params.put("DeliverySurname", lastname)
        params.put("DeliveryAddress1", requiresPayment.deliveryAddress.address1)
        params.put("DeliveryCity", requiresPayment.deliveryAddress.town)
        params.put("DeliveryPostCode", requiresPayment.deliveryAddress.postcode)
        params.put("DeliveryCountry", "GB")

        val billingName = Option(requiresPayment.billingAddress.name).getOrElse("")

        val fn = billingName.split(" ").last
        val ln = billingName.split(" ").head

        params.put("BillingSurname", fn)
        params.put("BillingFirstnames", ln)
        params.put("BillingAddress1", requiresPayment.billingAddress.address1)
        params.put("BillingCity", requiresPayment.billingAddress.town)
        params.put("BillingPostCode", requiresPayment.billingAddress.postcode)
        params.put("BillingCountry", "GB")

        if (requiresPayment.isInstanceOf[Basket])
            params.put("Basket", sageBasketString(requiresPayment.asInstanceOf[Basket]))

        logger.debug("Params [{}]", params)
        params.toMap
    }

    def paymentUrl: String = LiveUrl

}
