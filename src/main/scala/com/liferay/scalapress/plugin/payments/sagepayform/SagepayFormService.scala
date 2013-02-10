package com.liferay.scalapress.plugin.payments.sagepayform

import org.apache.commons.codec.binary.Base64
import com.liferay.scalapress.Logging
import com.liferay.scalapress.plugin.ecommerce.domain.{Payment, Basket}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object SagepayFormService extends Logging {

    val PaymentTypePayment = "PAYMENT"
    val PaymentTypeDeferred = "DEFERRED"
    val VPSProtocol: String = "2.23"

    val LiveUrl = "https://live.sagepay.com/gateway/service/vspform-register.vsp"
    val TestUrl = "https://test.sagepay.com/gateway/service/vspform-register.vsp"

    def xor(in: Array[Byte], key: String) = {
        val result = new Array[Byte](in.length)
        for (k <- 0 until in.length) {
            val b = in(k) ^ key.charAt(k % key.length)
            result(k) = b.toByte
        }
        result
    }

    def decrypt(plugin: SagepayFormPlugin, base64: String): Map[String, String] = {

        val encrypted = Base64.decodeBase64(base64.getBytes)
        val x = xor(encrypted, plugin.sagePayEncryptionPassword)
        val decrypted = new String(x)
        val params = decrypted.split("&").map(_.split("=")).filter(_.size == 2).map(a => (a(0), a(1))).toMap
        params
    }

    def encrypt(plugin: SagepayFormPlugin, params: Map[String, String]): String = {
        val data = params.map(e => e._1 + "=" + e._2).mkString("&")
        val x = xor(data.getBytes, plugin.sagePayEncryptionPassword)
        val base64 = Base64.encodeBase64(x)
        new String(base64)
    }

    // returns the four params needed by sagepay
    def params(basket: Basket, plugin: SagepayFormPlugin): Map[String, String] = {

        val crypt = encrypt(plugin, cryptParams(plugin, basket))

        val params = Map("VPSProtocol" -> VPSProtocol,
            "TxType" -> PaymentTypePayment,
            "Vendor" -> plugin.sagePayVendorName,
            "Crypt" -> crypt)

        params
    }

    // creates a payment object for the given set of response params
    def createPayment(params: Map[String, String]) = {

        val orderId = params.get("VendorTxCode").getOrElse("0").toLong
        val transactionId = params.get("VPSTxId").orNull
        val authCode = params.get("TxAuthNo").orNull
        val amount: Int = (params.get("Amount").getOrElse("0").toDouble * 100).toInt

        val payment = new Payment
        payment.paymentType = "SagePayForm"
        payment.date = System.currentTimeMillis()
        payment.transactionId = transactionId
        payment.authCode = authCode
        payment.order = orderId
        payment.amount = amount
        payment
    }

    def basketString(basket: Basket) = {

        val numOfLines = basket.lines.size + 1

        val sb = new StringBuilder
        sb.append(numOfLines)

        for (line <- basket.lines.asScala) {
            sb.append(":")
            sb.append(line.obj.name.replaceAll("[=:&$*]", ""))
            sb.append(":")
            sb.append(line.qty)
            sb.append(":")
            sb.append(line.obj.sellPrice)
            sb.append(":")
            sb.append(line.obj.vat)
            sb.append(":")
            sb.append(line.obj.sellPriceInc)
            sb.append(":")
            sb.append(line.total)
        }

        sb.append(":")
        sb.append(basket.deliveryOption.name.replaceAll("[&:=$*]", ""))
        sb.append(":1:")
        sb.append(basket.deliveryOption.charge)
        sb.append(":")
        sb.append(basket.deliveryOption.chargeVat)
        sb.append(":")
        sb.append(basket.deliveryOption.chargeIncVat)
        sb.append(":")
        sb.append(basket.deliveryOption.chargeIncVat)


        sb.toString()
    }

    def processCallback(params: Map[Any, Any], plugin: SagepayFormPlugin): Option[Payment] = {
        logger.debug("Callback params")

        val crypt = params.get("crypt").getOrElse("").toString
        val p = decrypt(plugin, crypt)
        logger.debug("Sagepay params {}", p)

        val status = p.get("Status").getOrElse("NoStatus")

        status.toLowerCase match {
            case "ok" =>
                val sageTxId = p.get("VPSTxId").getOrElse("NoId")
                existingTransaction(sageTxId) match {
                    case true => None
                    case false => Some(createPayment(p))
                }
            case _ => None
        }
    }

    def existingTransaction(sageTxId: String) = false

    // returns the unencrpyted params used in the crypt field
    def cryptParams(plugin: SagepayFormPlugin, basket: Basket): Map[String, String] = {

        val params = new scala.collection.mutable.HashMap[String, String]
        params.put("VendorTxCode", basket.sessionId) // store basket id as our tx id
        params.put("Currency", "GBP")
        params.put("Amount", basket.total.toString)
        params.put("CustomerName", "sammy")
        params.put("CustomerEmail", "sam@sam.com")
        params.put("Description", "Payment for basket " + basket.sessionId)

        Option(plugin.sagePayVendorEmail).foreach(email => {
            params.put("VendorEmail", plugin.sagePayVendorEmail)
        })

        params.put("Description", "Order")
        params.put("SuccessURL", "http://www.satnaveasy.co.uk/checkout/payment/success")
        params.put("FailureURL", "http://www.satnaveasy.co.uk/checkout/payment/failure")

        params.put("Basket", basketString(basket))

        val firstname = basket.deliveryAddress.name.split(" ").last
        val lastname = basket.deliveryAddress.name.split(" ").head

        params.put("BillingSurname", firstname)
        params.put("BillingFirstnames", lastname)
        params.put("BillingAddress1", basket.deliveryAddress.address1)
        params.put("BillingCity", basket.deliveryAddress.town)
        params.put("BillingPostCode", basket.deliveryAddress.postcode)
        params.put("BillingCountry", "GB")
        //                if (basket.deliveryAddress.getCountry().equals(Country.US)) {
        //                    params.put("BillingState", basket.deliveryAddress.getState())
        //                }

        params.put("DeliveryFirstnames", firstname)
        params.put("DeliverySurname", lastname)
        params.put("DeliveryAddress1", basket.deliveryAddress.address1)
        params.put("DeliveryCity", basket.deliveryAddress.town)
        params.put("DeliveryPostCode", basket.deliveryAddress.postcode)
        params.put("DeliveryCountry", "GB")
        //        if (basket.deliveryAddress.getCountry().equals(Country.US)) {
        //            params.put("DeliveryState", basket.deliveryAddress.getState())
        //        }

        logger.debug("Params [{}]", params)
        params.toMap
    }
}
