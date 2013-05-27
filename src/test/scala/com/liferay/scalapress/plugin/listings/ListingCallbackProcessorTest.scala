package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.ecommerce._
import org.mockito.{ArgumentCaptor, Mockito}
import com.liferay.scalapress.settings.{Installation, InstallationDao}
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import com.liferay.scalapress.plugin.listings.email.ListingCustomerNotificationService
import com.liferay.scalapress.plugin.listings.domain.{ListingsPlugin, ListingPackage}
import com.liferay.scalapress.obj.{ObjectDao, Obj}
import com.liferay.scalapress.payments.{Transaction, TransactionDao}

/** @author Stephen Samuel */
class ListingCallbackProcessorTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val tx = new Transaction
    tx.id = 12

    val listing = new Obj
    listing.id = 96
    listing.status = Obj.STATUS_DISABLED
    listing.name = "big horse for sale"
    listing.listingPackage = new ListingPackage
    listing.listingPackage.id = 5472
    listing.listingPackage.fee = 15

    val installation = new Installation

    val plugin = new ListingsPlugin

    val callback = new ListingCallbackProcessor
    callback.context = new ScalapressContext
    callback.context.transactionDao = mock[TransactionDao]
    callback.listingsPluginDao = mock[ListingsPluginDao]
    Mockito.when(callback.listingsPluginDao.get).thenReturn(plugin)

    callback.context.installationDao = mock[InstallationDao]
    Mockito.when(callback.context.installationDao.get).thenReturn(installation)

    callback.context.objectDao = mock[ObjectDao]
    callback.orderDao = mock[OrderDao]
    callback.listingCustomerNotificationService = mock[ListingCustomerNotificationService]

    test("invoking with a string looks up the listing by id") {
        Mockito.when(callback.context.objectDao.find(123456)).thenReturn(listing)
        callback.callback(tx, "123456")
        Mockito.verify(callback.context.objectDao).find(123456)
    }

    test("given a tx then it is added to order") {
        callback.callback(Some(tx), listing)
        val captor = ArgumentCaptor.forClass(classOf[Order])
        Mockito.verify(callback.orderDao, Mockito.atLeastOnce).save(captor.capture)
        assert(captor.getValue.payments.contains(tx))
    }

    test("if a listing package is free then verify no transaction is saved") {
        listing.listingPackage.fee = 0
        callback.callback(Some(tx), listing)
        Mockito.verify(callback.context.transactionDao, Mockito.never).save(tx)
    }

    test("if the callback specifies no tx then verify no transaction is saved") {
        callback.callback(None, listing)
        Mockito.verify(callback.context.transactionDao, Mockito.never).save(tx)
    }

    test("if listing package is auto publish then change listing status to live") {
        listing.listingPackage.autoPublish = true
        callback.callback(None, listing)
        assert(listing.status == Obj.STATUS_LIVE)
    }

    test("if listing package is not auto publish then do not change listing status") {
        listing.listingPackage.autoPublish = false
        callback.callback(None, listing)
        assert(listing.status == Obj.STATUS_DISABLED)
    }

    test("order status is updated to paid") {
        val order = callback._order(listing)
        assert(Order.STATUS_PAID === order.status)
    }

    test("order uses internal ip") {
        val order = callback._order(listing)
        assert("127.0.0.1" === order.ipAddress)
    }

    test("order line is added from the listing details") {
        val order = callback._order(listing)
        val line = order.lines.get(0)
        assert(line.description.contains("#" + listing.id))
        assert(listing.listingPackage.fee === line.price)
    }

    test("emails are sent using the listing") {
        callback.callback(Some(tx), listing)
        Mockito.verify(callback.listingCustomerNotificationService).send(listing)
    }
}
