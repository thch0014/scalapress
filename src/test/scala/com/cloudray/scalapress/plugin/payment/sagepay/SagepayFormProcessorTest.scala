package com.cloudray.scalapress.plugin.payment.sagepay

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.payments.sagepayform.{SagepayFormProcessor, SagepayFormPlugin}
import com.cloudray.scalapress.plugin.ecommerce.domain.Address
import com.cloudray.scalapress.payments.Purchase

/** @author Stephen Samuel */
class SagepayFormProcessorTest extends FunSuite with MockitoSugar with OneInstancePerTest {

    val plugin = new SagepayFormPlugin
    val processor = new SagepayFormProcessor(plugin)

    val address = new Address
    address.name = "sambo"
    address.postcode = "SW10"
    address.town = "London"
    address.address1 = "100 redcliffe gardens"

    val purchase = new Purchase {
        def successUrl: String = "http://coldplay.com/successUrl.com"
        def failureUrl: String = "http://coldplay.com/failureUrl.com"
        def accountName: String = "sammy"
        def accountEmail: String = "snape@hp.com"
        def total: Int = 1567
        def uniqueIdent: String = "616116"
        def callback = "Donkey"
        def paymentDescription: String = "some payment"
        override def billingAddress: Option[Address] = Some(address)
        override def deliveryAddress: Option[Address] = Some(address)
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

    test("processor sets name from purchase") {
        val params = processor._cryptParams(purchase, "coldplay.com")
        assert("sammy" === params("CustomerName"))
    }

    test("processor sets email from purchase") {
        val params = processor._cryptParams(purchase, "coldplay.com")
        assert("snape@hp.com" === params("CustomerEmail"))
    }

    test("processor sets failure url from purchase") {
        val params = processor._cryptParams(purchase, "coldplay.com")
        assert("http://coldplay.com/failureUrl.com" === params("FailureURL"))
    }

    test("processor sets success url from purchase") {
        val params = processor._cryptParams(purchase, "coldplay.com")
        assert("http://coldplay.com/successUrl.com" === params("SuccessURL"))
    }

    test("processor sets billing address fields from purchase") {
        val params = processor._cryptParams(purchase, "coldplay.com")
        assert("sambo" === params("BillingSurname"))
        assert("100 redcliffe gardens" === params("BillingAddress1"))
        assert("London" === params("BillingCity"))
        assert("SW10" === params("BillingPostCode"))
    }

    test("processor sets delivery address fields from purchase") {
        val params = processor._cryptParams(purchase, "coldplay.com")
        assert("sambo" === params("DeliverySurname"))
        assert("100 redcliffe gardens" === params("DeliveryAddress1"))
        assert("London" === params("DeliveryCity"))
        assert("SW10" === params("DeliveryPostCode"))
    }

    test("processor sets callback info into the VendorTxCode field") {

        val params = processor._cryptParams(purchase, "coldplay.com")
        assert(params("VendorTxCode") === "Donkey-616116")
    }

    test("test encrypt and decrypt are invertible") {
        plugin.sagePayEncryptionPassword = "superpass"
        val encrypted = processor.encryptParams(Map("name" -> "sammy", "location" -> "chelsea"))
        val decrypted = processor.decryptParams(encrypted)
        assert(decrypted("name") === "sammy")
        assert(decrypted("location") === "chelsea")
    }
}
