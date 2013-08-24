package com.cloudray.scalapress.plugin.ecommerce

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Obj
import org.mockito.{ArgumentCaptor, Mockito}
import com.cloudray.scalapress.plugin.ecommerce.controller.admin.OrderQuery
import org.scalatest.junit.JUnitRunner

/** @author Stephen Samuel */

class SalesReporterTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val reporter = new SalesReporter
  reporter.orderDao = mock[OrderDao]

  "a sales report" should "query by date range" in {
    reporter.generate(Obj.STATUS_LIVE, 10000, 20000)
    val captor = ArgumentCaptor.forClass(classOf[OrderQuery])
    Mockito.verify(reporter.orderDao).search(captor.capture)
    assert(captor.getValue.from === 10000)
    assert(captor.getValue.to === 20000)
  }
}
