package com.liferay.scalapress.plugin.listings

import org.scalatest.{OneInstancePerTest, FunSuite}
import org.scalatest.mock.MockitoSugar
import com.liferay.scalapress.ScalapressContext
import com.liferay.scalapress.plugin.ecommerce._
import org.mockito.{ArgumentCaptor, Mockito}
import com.liferay.scalapress.settings.{Installation, InstallationDao}
import com.liferay.scalapress.plugin.payments.{TransactionDao, Transaction}
import com.liferay.scalapress.plugin.ecommerce.domain.Order
import com.liferay.scalapress.plugin.listings.email.{ListingAdminNotificationService, ListingCustomerNotificationService}
import com.liferay.scalapress.plugin.listings.domain.{ListingsPlugin, ListingPackage, ListingProcess}
import com.liferay.scalapress.obj.{ObjectDao, Obj}

/** @author Stephen Samuel */
class ListingCallbackProcessorTest extends FunSuite with OneInstancePerTest with MockitoSugar {

    val tx = new Transaction
    tx.id = 12

    val listing = new Obj
    listing.id = 96
    listing.status = Obj.STATUS_DISABLED
    listing.name = "big horse for sale"

    val process = new ListingProcess
    process.sessionId = "2355"
    process.listing = listing
    process.listingPackage = new ListingPackage
    process.listingPackage.id = 5472
    process.listingPackage.fee = 15

    val installation = new Installation

    val plugin = new ListingsPlugin

    val callback = new ListingCallbackProcessor
    callback.context = new ScalapressContext
    callback.context.transactionDao = mock[TransactionDao]
    callback.listingsPluginDao = mock[ListingsPluginDao]
    callback.listingProcessDao = mock[ListingProcessDao]
    Mockito.when(callback.listingsPluginDao.get).thenReturn(plugin)

    callback.context.installationDao = mock[InstallationDao]
    Mockito.when(callback.context.installationDao.get).thenReturn(installation)

    callback.context.objectDao = mock[ObjectDao]
    callback.orderDao = mock[OrderDao]
    callback.listingCustomerNotificationService = mock[ListingCustomerNotificationService]
    callback.listingAdminNotificationService = mock[ListingAdminNotificationService]

    test("given a tx then it is added to order") {
        callback.callback(Some(tx), process)
        val captor = ArgumentCaptor.forClass(classOf[Order])
        Mockito.verify(callback.orderDao, Mockito.atLeastOnce).save(captor.capture)
        assert(captor.getValue.payments.contains(tx))
    }

    test("if a listing package is free then verify no transaction is saved") {
        process.listingPackage.fee = 0
        callback.callback(Some(tx), process)
        Mockito.verify(callback.context.transactionDao, Mockito.never).save(tx)
    }

    test("if the callback specifies no tx then verify no transaction is saved") {
        callback.callback(None, process)
        Mockito.verify(callback.context.transactionDao, Mockito.never).save(tx)
    }

    test("if listing package is auto publish then change listing status") {
        process.listingPackage.autoPublish = true
        callback.callback(None, process)
        assert(listing.status == Obj.STATUS_LIVE)
    }

    test("if listing package is not auto publish then do not change listing status") {
        process.listingPackage.autoPublish = false
        callback.callback(None, process)
        assert(listing.status == Obj.STATUS_DISABLED)
    }

    test("order status is updated to paid") {
        val order = callback._order(listing, process)
        assert(Order.STATUS_PAID === order.status)
    }

    test("emails are sent using the listing") {
        callback.callback(Some(tx), process)
        Mockito.verify(callback.listingCustomerNotificationService).send(process.listing, callback.context)
    }
}
