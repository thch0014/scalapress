package com.liferay.scalapress.plugin.payment.sagepay

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.payments.Purchase
import com.liferay.scalapress.plugin.payments.sagepayform.{SagepayFormProcessor, SagepayFormPlugin}

/** @author Stephen Samuel */
class SagepayFormProcessorTest extends FunSuite with MockitoSugar {

    val plugin = new SagepayFormPlugin
    val processor = new SagepayFormProcessor(plugin)

    val purchase = new Purchase {
        def successUrl: String = "successUrl.com"
        def failureUrl: String = "failureUrl.com"
        def accountName: String = "sammy"
        def accountEmail: String = "snape@hp.com"
        def total: Int = 1567
        def uniqueIdent: String = "616116"
        def callbackClass: Class[_] = classOf[java.lang.InstantiationError]
        def paymentDescription: String = "some payment"
    }

    test("given a parameter map with valid paypal fields then a transaction is created") {
        val params = Map("VPSTxId" -> "transactionid6655",
            "TxAuthNo" -> "authyauthy2523",
            "Amount" -> "1567.89",
            "mc_currency" -> "GBP",
            "txn_id" -> "739653939S128390K",
            "mc_gross" -> "20.00",
            "custom" -> "0a1d74c2-f809-4815-a53b-60a28e8da6a0")

        val tx = processor._createTx(params)
        assert(tx.transactionId === "transactionid6655")
        assert(tx.authCode === "authyauthy2523")
        assert(tx.amount === 156789)
        assert(tx.paymentProcessor === "SagePayForm")
    }

    test("the processor is enabled iff the plugin sagePayVendorName is not null") {
        plugin.sagePayVendorName = null
        assert(!plugin.enabled)

        plugin.sagePayVendorName = "sammy"
        assert(plugin.enabled)
    }

    test("processor sets description from purchase") {
        val params = processor._cryptParams(purchase, "coldplay.com")
        assert(params("Description") === "some payment")
    }

    test("processor sets amount from purchase") {
        val params = processor._cryptParams(purchase, "coldplay.com")
        assert("15.67" === params("Amount"))
    }

    test("processor sets callback info into the VendorTxCode field") {

        val params = processor._cryptParams(purchase, "coldplay.com")
        assert(params("VendorTxCode") === "java.lang.InstantiationError:616116")
    }
}
