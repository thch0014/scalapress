package com.liferay.scalapress.plugin.payment.paypal.standard

import org.scalatest.FunSuite
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.payments.paypal.standard.{PaypalStandardPlugin, PaypalStandardProcessor}

/** @author Stephen Samuel */
class PaypalStandardProcessorTest extends FunSuite with MockitoSugar {

    val plugin = mock[PaypalStandardPlugin]
    val processor = new PaypalStandardProcessor(plugin)

    test("create payment happy path") {
        val params = Map("payment_status" -> "Completed",
            "payer_status" -> "unverified",
            "payment_type" -> "instant",
            "mc_currency" -> "GBP",
            "txn_id" -> "739653939S128390K",
            "payment_gross" -> "20.00",
            "custom" -> "0a1d74c2-f809-4815-a53b-60a28e8da6a0")

        val tx = processor._createPayment(params)
        assert(tx.transactionId === "739653939S128390K")
        assert(tx.amount === 2000)
        assert(tx.currency === "GBP")
        assert(tx.status === "Completed")
        assert(tx.payerStatus === "unverified")
    }

}
