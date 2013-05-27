package com.liferay.scalapress.plugin.payment.worldpay.selectjunior

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.payments.Purchase
import com.liferay.scalapress.plugin.payments.worldpay.selectjunior.{WorldpaySelectJuniorPlugin, WorldpaySelectJuniorProcessor}

/** @author Stephen Samuel */
class WorldpaySelectJuniorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val plugin = new WorldpaySelectJuniorPlugin
    val processor = new WorldpaySelectJuniorProcessor(plugin)

    val purchase = new Purchase {
        def successUrl: String = "http://coldplay.com/successUrl.com"
        def failureUrl: String = "http://coldplay.com/failureUrl.com"
        def accountName: String = "sammy"
        def accountEmail: String = "snape@hp.com"
        def total: Int = 58233
        def uniqueIdent: String = "56789"
        def callback = "Monkey"
        def paymentDescription: String = "some payment"
    }

    test("given a parameter map with valid paypal fields then a transaction is created") {
        val params = Map("transStatus" -> "Y",
            "callbackPW" -> "letmein",
            "cardType" -> "visa",
            "transId" -> "73965qweqwe128390K",
            "authAmount" -> "20.00",
            "rawAuthCode" -> "0a1d74c2-f809-4815-a53b-60a28e8da6a0")

        plugin.callbackPassword = "letmein"
        val tx = processor.createTransaction(params).get
        assert(tx.transactionId === "73965qweqwe128390K")
        assert(tx.amount === 2000)
        assert(tx.authCode === "0a1d74c2-f809-4815-a53b-60a28e8da6a0")
        assert(tx.status === "Y")
    }

    test("the processor is enabled iff the plugin accountId is not null") {
        plugin.accountId = null
        assert(!plugin.enabled)

        plugin.accountId = "sammy"
        assert(plugin.enabled)
    }

    test("when set to live then the live url is returned") {
        plugin.live = true
        assert("https://select.worldpay.com/wcc/purchase" === processor.paymentUrl)
    }

    test("processor sets callback info into the cartId field") {
        val params = processor.params("coldplay.com", purchase)
        assert(params("cartId") === "Monkey:56789")
    }

    test("processor sets description from purchase") {
        val params = processor.params("coldplay.com", purchase)
        assert("some payment" === params("desc"))
    }

    test("processor sets amount from purchase") {
        val params = processor.params("coldplay.com", purchase)
        assert("582.33" === params("amount"))
    }

    test("processor sets name from purchase") {
        val params = processor.params("coldplay.com", purchase)
        assert("sammy" === params("name"))
    }

    test("processor sets email from purchase") {
        val params = processor.params("coldplay.com", purchase)
        assert("snape@hp.com" === params("email"))
    }

    //    test("processor sets failure url from purchase") {
    //        val params = processor.params("coldplay.com", purchase)
    //        assert("http://coldplay.com/successUrl.com" === params("return"))
    //    }
    //
    //    test("processor sets success url from purchase") {
    //        val params = processor.params("coldplay.com", purchase)
    //        assert("http://coldplay.com/failureUrl.com" === params("cancel_return"))
    //    }

    //    test("callback result parses the custom value correctly") {
    //        val result = processor
    //          .callback(Map("custom" -> "Order:567",
    //            "txn_id" -> "6346aa",
    //            "payment_status" -> "Completed",
    //            "payer_status" -> "unverified",
    //            "mc_currency" -> "GBP",
    //            "payment_type" -> "instant"))
    //        assert(result.get.uniqueId === "567")
    //        assert(result.get.callback === "Order")
    //    }
}
