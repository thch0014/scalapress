package com.liferay.scalapress.plugin.payments.sagepayform

import org.apache.commons.codec.binary.Base64
import com.liferay.scalapress.Logging
import com.liferay.scalapress.plugin.ecommerce.domain.{Payment, Basket}
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/** @author Stephen Samuel */
object SagepayFormService extends Logging {

    val PaymentTypePayment = "PAYMENT"
    val PaymentTypeDeferred = "DEFERRED"
    val VPSProtocol: String = "3.0"

    val LiveUrl = "https://live.sagepay.com/gateway/service/vspform-register.vsp"
    val TestUrl = "https://test.sagepay.com/gateway/service/vspform-register.vsp"

    def decrypt(plugin: SagepayFormPlugin, base64: String): Map[String, String] = {

        val encrypted = Base64.decodeBase64(base64.getBytes)
        val keySpec = new SecretKeySpec(plugin.sagePayEncryptionPassword.getBytes, "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)

        val decrypted = new String(cipher.doFinal(encrypted))
        val params = decrypted.split("&").map(_.split("=")).filter(_.size == 2).map(a => (a(0), a(1))).toMap
        params
    }

    def encrypt(plugin: SagepayFormPlugin, params: Map[String, String]): String = {

        val data = params.map(e => e._1 + "=" + e._2).mkString("&")

        val keySpec = new SecretKeySpec(plugin.sagePayEncryptionPassword.getBytes, "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)

        val encrypted = cipher.doFinal(data.getBytes)
        val base64 = Base64.encodeBase64(encrypted)
        new String(base64)
    }

    // returns the four params needed by sagepay
    def params(plugin: SagepayFormPlugin): Map[String, String] = {

        val crypt = encrypt(plugin, cryptParams(plugin, null))

        val params = Map("VPSProtocol" -> "2.23",
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
        payment.orderId = orderId
        payment
    }

    def basketString(basket: Basket) = {

        val sb = new StringBuilder
        //        Integer numOfLines = lines.size();
        //        if (order.hasDeliveryDetails()) {
        //            numOfLines ++;
        //        }
        //        sb.append(numOfLines);
        //
        //        for (OrderLine line: lines)
        //        {
        //            sb.append(":");
        //            sb.append(line.getDescription().replaceAll("[:=&]", ""));
        //            sb.append(":");
        //            sb.append(line.getQty());
        //            sb.append(":");
        //            sb.append(line.getUnitSellEx());
        //            sb.append(":");
        //            sb.append(line.getUnitSellVat());
        //            sb.append(":");
        //            sb.append(line.getUnitSellInc());
        //            sb.append(":");
        //            sb.append(line.getLineSellInc());
        //        }
        //        if (order.hasDeliveryDetails()) {
        //            sb.append(":");
        //            sb.append(order.getDeliveryDetails().replaceAll("[:=&]", ""));
        //            sb.append(":1:");
        //            sb.append(order.getDeliveryChargeEx());
        //            sb.append(":");
        //            sb.append(order.getDeliveryChargeVat());
        //            sb.append(":");
        //            sb.append(order.getDeliveryChargeInc());
        //            sb.append(":");
        //            sb.append(order.getDeliveryChargeInc());
        //        }

        sb.toString()
    }

    // returns the unencrpyted params used in the crypt field
    def cryptParams(plugin: SagepayFormPlugin, basket: Basket): Map[String, String] = {

        val params = new scala.collection.mutable.HashMap[String, String]

        // params +=("VendorTxCode", session.getTransactionId)

        params.put("Currency", "GBP")
        params.put("Amount", basket.total.toString)
        params.put("CustomerName", "sammy")
        params.put("CustomerEmail", "sam@sam.com")
        params.put("Description", "Customer Order")

        Option(plugin.sagePayVendorEmail).foreach(email => {
            params.put("VendorEmail", plugin.sagePayVendorEmail)
        })

        params.put("Description", "Order")
        params.put("SuccessURL", "/payment-callback-sagepayform/success")
        params.put("FailureURL", "/payment-callback-sagepayform/failure")

        params.put("Basket", basketString(basket))

        //        params.put("BillingSurname", order.getBillingAddress().getLastName())
        //        params.put("BillingFirstnames", order.getBillingAddress().getFirstName())
        //        params.put("BillingAddress1", order.getBillingAddress().getAddressLine1())
        //        params.put("BillingCity", order.getBillingAddress().getTown())
        //        params.put("BillingPostCode", order.getBillingAddress().getPostcode())
        //        params.put("BillingCountry", order.getBillingAddress().getCountry().getIsoAlpha2())
        //        if (order.getBillingAddress().getCountry().equals(Country.US)) {
        //            params.put("BillingState", order.getBillingAddress().getState())
        //        }

        //        params.put("DeliveryFirstnames", order.getDeliveryAddress().getFirstName())
        //        params.put("DeliverySurname", order.getDeliveryAddress().getLastName())
        //        params.put("DeliveryAddress1", order.getDeliveryAddress().getAddressLine1())
        //        params.put("DeliveryCity", order.getDeliveryAddress().getTown())
        //        params.put("DeliveryPostCode", order.getDeliveryAddress().getPostcode())
        //        params.put("DeliveryCountry", order.getDeliveryAddress().getCountry().getIsoAlpha2())
        //        if (order.getDeliveryAddress().getCountry().equals(Country.US)) {
        //            params.put("DeliveryState", order.getDeliveryAddress().getState())
        //        }

        logger.debug("Params [{}]", params)
        params.toMap
    }
}
