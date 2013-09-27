package com.cloudray.scalapress.payments

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.ScalapressContext
import org.mockito.Mockito
import com.cloudray.scalapress.settings.{Installation, InstallationDao}

/** @author Stephen Samuel */
class PaymentFormRendererTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val renderer = new PaymentFormRenderer
  renderer.context = new ScalapressContext
  renderer.context.paymentPluginDao = mock[PaymentPluginDao]
  renderer.context.installationDao = mock[InstallationDao]

  val installation = new Installation
  installation.name = "coldplay tees"

  Mockito.when(renderer.context.installationDao.get).thenReturn(installation)

  val purchase = new Purchase {
    def total: Int = 1543
    def uniqueIdent: String = "5152"
    def callback: String = ""
    def paymentDescription: String = "something to buy"
    def accountEmail: String = "sam@snake.com"
    def failureUrl: String = "failure.com"
    def successUrl: String = "success.com"
    def accountName: String = "sammy"
  }

  test("renderer renders all enabled payments") {
    val plugin1 = new MockPaymentPlugin("superpay", true, Map.empty)
    val plugin2 = new MockPaymentPlugin("megapay", true, Map.empty)
    Mockito.when(renderer.context.paymentPluginDao.enabled).thenReturn(Seq(plugin1, plugin2))
    val form = renderer.renderPaymentForm(purchase).toString()
    assert(form.contains("superpay"))
    assert(form.contains("http://www.superpay.com/payment"))
    assert(form.contains("megapay"))
    assert(form.contains("http://www.megapay.com/payment"))
  }

  test("renderer renders params for each plugin") {
    val plugin1 = new
        MockPaymentPlugin("superpay", true,
          Map("invoice" -> "inv154", "email" -> "rambo@firstblood.com", "cartId" -> "55aa"))
    Mockito.when(renderer.context.paymentPluginDao.enabled).thenReturn(Seq(plugin1))
    val form = renderer.renderPaymentForm(purchase).toString()
    assert(form.contains("<input type=\"hidden\" name=\"cartId\" value=\"55aa\"/>"))
    assert(form.contains("<input type=\"hidden\" name=\"email\" value=\"rambo@firstblood.com\"/>"))
    assert(form.contains("<input type=\"hidden\" name=\"invoice\" value=\"inv154\"/>"))
  }
}

class MockPaymentPlugin(val n: String, val e: Boolean, val p: Map[String, String]) extends PaymentPlugin {
  def enabled: Boolean = e
  def processor: PaymentProcessor = new PaymentProcessor {
    def callback(params: Map[String, String]): Option[CallbackResult] = None
    def paymentUrl: String = "http://www." + n + ".com/payment"
    def paymentProcessorName: String = n
    def params(domain: String, purchase: Purchase): Map[String, String] = p
  }
  def name: String = n
}