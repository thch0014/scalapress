package com.cloudray.scalapress.plugin.payment.paypal.standard

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.payments.paypal.standard.PaypalStandardPlugin

/** @author Stephen Samuel */
class PaypalStandardPluginTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val plugin = new PaypalStandardPlugin

  test("paypal is disabled if account email is not set") {
    plugin.accountEmail = null
    assert(!plugin.enabled)
  }

  test("paypal is enabled if account email is set") {
    plugin.accountEmail = "sammy@pay.com"
    assert(plugin.enabled)
  }
}
