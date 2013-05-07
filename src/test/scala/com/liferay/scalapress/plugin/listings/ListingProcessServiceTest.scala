package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.plugin.listings.controller.ListingProcessService
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.obj.Obj
import scala.collection.JavaConverters._
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.ecommerce.OrderDao
import org.mockito.Mockito

/** @author Stephen Samuel */
class ListingProcessServiceTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val req = mock[HttpServletRequest]
    val process = new ListingProcess
    process.listingPackage = new ListingPackage
    process.listingPackage.name = "superpack"
    val account = new Obj
    account.id = 123

    val plugin = new ListingsPlugin

    val service = new ListingProcessService()
    service.context = new ScalapressContext
    service.context.orderDao = mock[OrderDao]

    test("order uses vat rate from listing plugin") {
        val order = service._order(account, process, req, plugin)
        val actual = order.lines.asScala.head.vatRate
        assert(0 === actual)
    }

    test("order line uses name from listing package as description") {
        val order = service._order(account, process, req, plugin)
        val actual = order.lines.asScala.head.description
        assert("superpack" === actual)
    }

    test("order is saved") {
        val order = service._order(account, process, req, plugin)
        Mockito.verify(service.context.orderDao, Mockito.atLeastOnce).save(order)
    }
}
