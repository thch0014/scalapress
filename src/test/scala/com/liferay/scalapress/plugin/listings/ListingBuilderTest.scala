package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import javax.servlet.http.HttpServletRequest
import com.liferay.scalapress.obj.Obj
import scala.collection.JavaConverters._
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.ecommerce.OrderDao
import org.mockito.Mockito
import com.liferay.scalapress.plugin.listings.domain.{ListingsPlugin, ListingPackage, ListingProcess}

/** @author Stephen Samuel */
class ListingBuilderTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val req = mock[HttpServletRequest]
    val process = new ListingProcess
    process.listingPackage = new ListingPackage
    process.listingPackage.name = "superpack"
    val account = new Obj
    account.id = 123

    val listing = new Obj
    listing.id = 565
    listing.name = "my listing"

    val plugin = new ListingsPlugin

    val service = new ListingProcessService()
    service.context = new ScalapressContext
    service.context.orderDao = mock[OrderDao]

    test("order uses vat rate from listing plugin") {
        val order = service._order(account, listing, process, plugin)
        val actual = order.lines.asScala.head.vatRate
        assert(0 === actual)
    }

    test("order line uses name from listing package as description") {
        val order = service._order(account, listing, process, plugin)
        val actual = order.lines.asScala.head.description
        assert("superpack Listing #565" === actual)
    }

    test("order line includes listing # in the description") {
        listing.id = 1555555
        val order = service._order(account, listing, process, plugin)
        val actual = order.lines.asScala.head.description
        assert(actual.contains("1555555"))
    }

    test("order is saved") {
        val order = service._order(account, listing, process, plugin)
        Mockito.verify(service.context.orderDao, Mockito.atLeastOnce).save(order)
    }
}
