package com.cloudray.scalapress.plugin.payment.sagepay

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.plugin.payments.sagepayform.SagepayFormPlugin

/** @author Stephen Samuel */
class SagepayPluginTest extends FunSuite with MockitoSugar with OneInstancePerTest {

  val plugin = new SagepayFormPlugin

  test("sagepay is disabled if sagePayVendorName is not set") {
    plugin.sagePayVendorName = null
    assert(!plugin.enabled)
  }

  test("sagepay is enabled if sagePayVendorName is set") {
    plugin.sagePayVendorName = "sammy@pay.com"
    assert(plugin.enabled)
  }
}
