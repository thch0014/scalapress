package com.cloudray.scalapress.payments

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import org.mockito.{Matchers, Mockito}
import com.cloudray.scalapress.framework.ScalapressContext
import com.cloudray.scalapress.payments.Transaction

/** @author Stephen Samuel */
class PaymentCallbackServiceTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val context = mock[ScalapressContext]
  val service = new PaymentCallbackService(context)

  val p = mock[PaymentPluginDao]
  val t = mock[TransactionDao]
  val callback = mock[PaymentCallback]

  Mockito.when(context.paymentPluginDao).thenReturn(p)
  Mockito.when(context.transactionDao).thenReturn(t)

  val tx = new Transaction

  val plugin1 = new MockPaymentPlugin("superpay", true, Map.empty)

  test("given a payment plugin that returns a transaction then that transaction is persisted") {

    val plugin2 = new PaymentPlugin {
      def enabled: Boolean = true
      def processor: PaymentProcessor = new PaymentProcessor {
        def callback(params: Map[String, String]): Option[CallbackResult] = Some(CallbackResult(tx, "Order-14"))
        def paymentUrl: String = "payment.com"
        def paymentProcessorName: String = "megapay"
        def params(domain: String, purchase: Purchase): Map[String, String] = Map.empty
      }
      def name: String = "megapay"
    }

    Mockito.when(p.enabled).thenReturn(Seq(plugin1, plugin2))
    service.callbacks(Map.empty[String, String])
    Mockito.verify(t).save(tx)
    Mockito.verify(t, Mockito.only).save(Matchers.any[Transaction])
  }

  test("given a payment plugin that that returns a callback type then that type is lookup up and invoked") {

    val plugin2 = new PaymentPlugin {
      def enabled: Boolean = true
      def processor: PaymentProcessor = new PaymentProcessor {
        def callback(params: Map[String, String]): Option[CallbackResult] = Some(CallbackResult(tx, "Pizza-14"))
        def paymentUrl: String = "payment.com"
        def paymentProcessorName: String = "megapay"
        def params(domain: String, purchase: Purchase): Map[String, String] = Map.empty
      }
      def name: String = "megapay"
    }

    val callback = mock[MockPaymentCallback]
    Mockito.when(p.enabled).thenReturn(Seq(plugin1, plugin2))
    Mockito.when(context.bean(classOf[MockPaymentCallback])).thenReturn(callback)

    service.callbacks(Map.empty[String, String])
    Mockito.verify(context).bean(classOf[MockPaymentCallback])
    Mockito.verify(callback).callback(tx, "14")
  }
}

@Callback("Pizza")
class MockPaymentCallback extends PaymentCallback {
  def callback(tx: Transaction, id: String) {}
}