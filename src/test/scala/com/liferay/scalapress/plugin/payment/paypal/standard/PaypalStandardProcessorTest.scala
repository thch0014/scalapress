package com.liferay.scalapress.plugin.payment.paypal.standard

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.payments.paypal.standard.{PaypalStandardPlugin, PaypalStandardProcessor}
import com.liferay.scalapress.plugin.payments.Purchase

/** @author Stephen Samuel */
class PaypalStandardProcessorTest extends FunSuite with MockitoSugar {

    val plugin = new PaypalStandardPlugin
    val processor = new PaypalStandardProcessor(plugin)

    val purchase = new Purchase {
        def successUrl: String = "successUrl.com"
        def failureUrl: String = "failureUrl.com"
        def accountName: String = "sammy"
        def accountEmail: String = "snape@hp.com"
        def total: Int = 58233
        def uniqueIdent: String = "56789"
        def callbackClass: Class[_] = classOf[ThreadDeath]
        def paymentDescription: String = "some payment"
    }

    test("given a parameter map with valid paypal fields then a transaction is created") {
        val params = Map("payment_status" -> "Completed",
            "payer_status" -> "unverified",
            "payment_type" -> "instant",
            "mc_currency" -> "GBP",
            "txn_id" -> "739653939S128390K",
            "mc_gross" -> "20.00",
            "custom" -> "0a1d74c2-f809-4815-a53b-60a28e8da6a0")

        val tx = processor._createTx(params)
        assert(tx.transactionId === "739653939S128390K")
        assert(tx.amount === 2000)
        assert(tx.currency === "GBP")
        assert(tx.status === "Completed")
        assert(tx.payerStatus === "unverified")
    }

    test("the processor is enabled iff the plugin account email is not null") {
        plugin.accountEmail = null
        assert(!plugin.enabled)

        plugin.accountEmail = "sammy"
        assert(plugin.enabled)
    }

    test("when set to production then the production url is returned") {
        plugin.production = true
        assert("https://www.paypal.com/cgi-bin/webscr" === processor.paymentUrl)
    }

    test("when not set to production then the sandbox url is returned") {
        plugin.production = false
        assert("https://www.sandbox.paypal.com/cgi-bin/webscr" === processor.paymentUrl)
    }

    test("processor sets callback info into the custom field") {
        val params = processor.params("coldplay.com", purchase)
        assert(params("custom") === "java.lang.ThreadDeath:56789")
    }

    test("processor sets description from purchase") {
        val params = processor.params("coldplay.com", purchase)
        assert("some payment" === params("item_name"))
    }

    test("processor sets amount from purchase") {
        val params = processor.params("coldplay.com", purchase)
        assert("582.33" === params("amount"))
    }

    test("processor sets invoice to unique id") {
        val params = processor.params("coldplay.com", purchase)
        assert("56789" === params("invoice"))
    }

    test("callback result parses the custom value correctly") {
        val result = processor
          .callback(Map("custom" -> "java.lang.String:567",
            "txn_id" -> "6346aa",
            "payment_status" -> "Completed",
            "payer_status" -> "unverified",
            "mc_currency" -> "GBP",
            "payment_type" -> "instant"))
        assert(result.get.uniqueId === "567")
        assert(result.get.callbackClass === Class.forName("java.lang.String"))
    }
}
