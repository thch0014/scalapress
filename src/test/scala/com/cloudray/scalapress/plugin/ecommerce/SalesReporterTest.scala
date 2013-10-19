package com.cloudray.scalapress.plugin.ecommerce

import org.scalatest.{FlatSpec, OneInstancePerTest}
import org.scalatest.mock.MockitoSugar
import com.cloudray.scalapress.obj.Obj
import org.mockito.{Matchers, ArgumentCaptor, Mockito}
import com.cloudray.scalapress.plugin.ecommerce.controller.admin.OrderQuery
import com.sksamuel.scoot.soa.Page
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.cloudray.scalapress.account.Account

/** @author Stephen Samuel */

class SalesReporterTest extends FlatSpec with MockitoSugar with OneInstancePerTest {

  val reporter = new SalesReporter
  reporter.orderDao = mock[OrderDao]

  Mockito.when(reporter.orderDao.search(Matchers.any[OrderQuery])).thenReturn(Page.empty[Order])

  "a sales report" should "query by date range" in {
    reporter.generate(Obj.STATUS_LIVE, 10000, 20000)
    val captor = ArgumentCaptor.forClass(classOf[OrderQuery])
    Mockito.verify(reporter.orderDao).search(captor.capture)
    assert(captor.getValue.from.get === 10000)
    assert(captor.getValue.to.get === 20000)
  }

  "a sales report" should "query by status" in {
    reporter.generate("qweqwe", 10000, 20000)
    val captor = ArgumentCaptor.forClass(classOf[OrderQuery])
    Mockito.verify(reporter.orderDao).search(captor.capture)
    assert(captor.getValue.status.get === "qweqwe")
  }

  "a sales report" should "be mapped to report line" in {
    val order = new Order
    order.id = 444
    order.datePlaced = 1234124214
    order.account = new Account
    order.account.name = "sammy"
    order.account.email = "sammy@fatty.com"
    order.customerNote = "crappy order"
    Mockito.when(reporter.orderDao.search(Matchers.any[OrderQuery])).thenReturn(Page.apply(Seq(order)))

    val reports = reporter.generate("qweqwe", 10000, 20000)
    assert(1 === reports.size)
    assert(1234124214 === reports(0).datePlaced)
    assert("444" === reports(0).orderId)
    assert("sammy@fatty.com" === reports(0).email)
    assert("sammy" === reports(0).name)
    assert("crappy order" === reports(0).note)
  }
}
