package com.cloudray.scalapress.plugin.ecommerce

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar

/** @author Stephen Samuel */

class SalesReportCsvWriterTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  "a sales csv writer" should "write valid headers" in {
    val headers = SalesReportCsvWriter._headers
    assert("orderId" === headers(0))
    assert("date" === headers(1))
    assert("name" === headers(2))
    assert("email" === headers(3))
    assert("status" === headers(4))
    assert("subtotal" === headers(5))
    assert("vat" === headers(6))
    assert("total" === headers(7))
  }
}

