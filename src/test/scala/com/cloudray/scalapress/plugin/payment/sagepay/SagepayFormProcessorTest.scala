package com.cloudray.scalapress.plugin.payment.sagepay

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.payments.sagepayform.{SagepayFormProcessor, SagepayFormPlugin}
import com.cloudray.scalapress.plugin.ecommerce.domain.{BasketLine, DeliveryOption, Basket, Address}
import com.cloudray.scalapress.payments.Purchase
import com.cloudray.scalapress.item.Item

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

  test("given a parameter map with valid sage fields then a transaction is created") {
    val params = Map(
      "VPSTxId" -> "transactionid6655",
      "TxAuthNo" -> "authyauthy2523",
      "Amount" -> "1567.89",
      "AVSCV2" -> "AllMatch",
      "CardType" -> "Visa",
      "StatusDetail" -> "Auth Success",
      "VendorTxCode" -> "Order-45558"
    )

    val tx = processor.createTx(params)
    assert(tx.transactionId === "transactionid6655")
    assert(tx.authCode === "authyauthy2523")
    assert(tx.amount === 156789)
    assert(tx.processor === "SagePayForm")
    assert(tx.securityCheck === "AllMatch")
    assert(tx.cardType === "Visa")
    assert(tx.details === "Auth Success")
    assert(tx.reference === "Order-45558")
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

  test("test split response string") {
    val input = "VendorTxCode=Order-102727&VPSTxId={6AA6DE3C-B694-7A80-5BAC-96D4EF1A8984}&Status=OK&StatusDetail=0000 : The Authorisation was Successful.&TxAuthNo=821316448&AVSCV2=SECURITY CODE MATCH ONLY&AddressResult=NOTMATCHED&PostCodeResult=MATCHED&CV2Result=MATCHED&GiftAid=0&3DSecureStatus=OK&CAVV=AAABB5FZcAAAABmYlllwAAAAAAA=&CardType=VISA&Last4Digits=0658&Amount=59.70"
    val params = processor._splitResponseString(input)
    assert(15 === params.size)
    assert("OK" === params("Status"))
    assert("821316448" === params("TxAuthNo"))
    assert("Order-102727" === params("VendorTxCode"))
  }

  test("test decrypting params") {
    val base64 = "OxweBRwBIxcxCwkcTS4BFxIdX1VdS0dTRFUhPyEwFTAUXAhFNi5EIChKM0wxRU5bX1MsQUBMRjE2LF9dWz1EJDVCNldLXFkEVjIHEgMaAVkiMlYyBxIDGgEgCA0RCB9OR19CVE1DUDUbFlcuBxAFFgIIABIDBh0KTQ4RElMgAgwRAR4KFhQfXVE7CiUYDRgvHE5PXUNXXE9EVUtVNjkhJztLTTI2MCI9OzA0WTMuNzZXIjMwLjFQLj0/LkkzAAkLFRIAIRIcBwgZRD4uJz42OzEsKD1WMRwAAywdAAgrFRIGHwNSPyU5OjgkN1U0OUA2CAoFDQdOOi4mJyU8NEc0GhEbMw0JREBHQDckChERHxwjFRIHAhxPKyZfMyAlJUouMyUvO0UnKRA2LjMlLxQpDR8fAC4zJSw4MSBOVTQOAAA5AAAETiU+PDNCIRgDFUc3HggbEB5EQFdGS1EuHwsYFwRcRkpZWEI="
    plugin.sagePayEncryptionPassword = "mypassword"
    val params = processor.decryptParams(base64)
    assert(15 === params.size)
    assert("OK" === params("Status"))
    assert("821316448" === params("TxAuthNo"))
    assert("Order-102727" === params("VendorTxCode"))
  }

  test("test decrypting params with spaces") {
    val base64 = "Hi4fCVhHYwo2WBQxcCshAy05XFkCAA5AU2EgBxkcGgN1MEBbdXMBRzABXWB JWFKcHwwWxoGBEdFGkQXeVcWJgt RFoPc0pUJkMRIDgXbigDbSIZVkFCATFSBDUkCG5XeHtBTQ0VYxoQFzEhOQw8FSE4EBleWllSAlYDdB4RMAQtOAILQlkZVCFPMSE5DB0IdXNHXA4HAkNNAVYVGzcQMXp2Iih0YGU7IW5QFwIgFkcFCiUufxV4PDluVhUpACECOzgjCERAWwZIejEADiwWI24bHh\n5DdlgWEGUVJzgIJ1oGBCUgdmF0OjBzVhcbVgECOz4dGQp4diY2fzUQayM6ATwKGAkKBRFBMWQVNzgWNjQ8KgUYRAh4OVN0MQIbWRImCQkzJVBiYDM0djEXDiMGNRISMCx2dHYzNApWFywWNzMxOxRQc3B7JjQRPDU EGcjISwYGUQIBEFAAFYVIAsmCTx2Q1wDGw5K"
    plugin.sagePayEncryptionPassword = "HKqm757ru7pTMdSg"
    val params = processor.decryptParams(base64)
    println(params)
    assert(7 === params.size)
    assert("OK" === params("Status"))
    assert("861925186" === params("TxAuthNo"))
    assert("Order-45592" === params("VendorTxCode"))
    assert("{16BF65E6-43A2-87A6-3350-4C43EAC5578F}" === params("VPSTxId"))
    assert("SECURITY CODE MATCH ONLY" === params("AVSCV2"))
  }

  test("test callback") {
    val base64 = "OxweBRwBIxcxCwkcTS4BFxIdX1VdS0dTRFUhPyEwFTAUXAhFNi5EIChKM0wxRU5bX1MsQUBMRjE2LF9dWz1EJDVCNldLXFkEVjIHEgMaAVkiMlYyBxIDGgEgCA0RCB9OR19CVE1DUDUbFlcuBxAFFgIIABIDBh0KTQ4RElMgAgwRAR4KFhQfXVE7CiUYDRgvHE5PXUNXXE9EVUtVNjkhJztLTTI2MCI9OzA0WTMuNzZXIjMwLjFQLj0/LkkzAAkLFRIAIRIcBwgZRD4uJz42OzEsKD1WMRwAAywdAAgrFRIGHwNSPyU5OjgkN1U0OUA2CAoFDQdOOi4mJyU8NEc0GhEbMw0JREBHQDckChERHxwjFRIHAhxPKyZfMyAlJUouMyUvO0UnKRA2LjMlLxQpDR8fAC4zJSw4MSBOVTQOAAA5AAAETiU+PDNCIRgDFUc3HggbEB5EQFdGS1EuHwsYFwRcRkpZWEI="
    plugin.sagePayEncryptionPassword = "mypassword"
    val result = processor.callback(Map("crypt" -> base64))
    assert(result.get.callbackInfo === "Order-102727")
    assert(result.get.tx.authCode === "821316448")
    assert(result.get.tx.transactionId === "{6AA6DE3C-B694-7A80-5BAC-96D4EF1A8984}")
  }

  test("params returns the mandatory parameters") {
    plugin.sagePayEncryptionPassword = "mypassword"
    val params = processor.params("coldplaylovers.com", purchase)
    assert(params.contains("VPSProtocol"))
    assert(params.contains("TxType"))
    assert(params.contains("Vendor"))
    assert(params.contains("Crypt"))
  }

  test("basket format is valid") {

    val basket = new Basket
    basket.deliveryOption = new DeliveryOption
    basket.deliveryOption.charge = 595
    basket.deliveryOption.name = "delivered by superman"

    val line = new BasketLine
    line.obj = new Item
    line.obj.price = 1999
    line.obj.vatRate = 20
    line.obj.name = "coldplay tshirt"
    line.qty = 2
    basket.lines.add(line)

    val string = processor.sageBasketString(basket)
    assert("2:coldplay tshirt:2:19.99:3.99:23.98:47.96:delivered by superman:1:5.95:0.0:5.95:5.95" === string)
  }
}
