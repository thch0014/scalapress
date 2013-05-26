package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.listings.domain.ListingProcess
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.ecommerce.domain.{OrderComment, OrderLine, Order}
import com.liferay.scalapress.{ScalapressContext, Logging}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.plugin.listings.email.{ListingCustomerNotificationService, ListingAdminNotificationService}
import com.liferay.scalapress.plugin.payments.{PaymentCallback, Transaction}
import scala.collection.JavaConverters._
import com.liferay.scalapress.plugin.ecommerce.OrderDao

/** @author Stephen Samuel */
@Component
class ListingCallbackProcessor extends PaymentCallback with Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var orderDao: OrderDao = _
    @Autowired var listingProcessDao: ListingProcessDao = _
    @Autowired var listingsPluginDao: ListingsPluginDao = _
    @Autowired var listingAdminNotificationService: ListingAdminNotificationService = _
    @Autowired var listingCustomerNotificationService: ListingCustomerNotificationService = _

    override def callback(tx: Transaction, id: String) {
        val process = listingProcessDao.find(id)
        callback(Option(tx), process)
    }

    def callback(tx: Option[Transaction], process: ListingProcess) {
        val listing = process.listing
        logger.debug("Performing listing callback [{}-{}]", listing.id, listing.name)

        if (process.listingPackage.autoPublish) {
            logger.debug("Auto publising listing [{}-{}]", listing.id, listing.name)
            listing.status = Obj.STATUS_LIVE
            context.objectDao.save(listing)
        }

        if (process.listingPackage.fee > 0) {

            val account = context.objectDao.find(process.accountId.toLong)
            val order = _order(account, process.listing, process)

            tx.foreach(tx => {
                context.transactionDao.save(tx)
                order.payments.add(tx)
                orderDao.save(order)
            })
        }

        _emails(process)
        _cleanup(process)
    }

    def _emails(process: ListingProcess) {

        logger.debug("Sending email to customer")
        listingCustomerNotificationService.send(process.listing, context)

        logger.debug("Sending email to admin")
        listingAdminNotificationService.notify(process.listing, process)
    }

    // empty the listing process
    def _cleanup(process: ListingProcess) {
        process.attributeValues.asScala.foreach(_.listingProcess = null)
        process.attributeValues.clear()
        logger.info("Process completed - removing from database")
        listingProcessDao.remove(process)
    }

    // build an order to hold the details of what the customer purchased
    def _order(account: Obj, listing: Obj, process: ListingProcess) = {
        logger.debug("Creating order for the listing")

        val order = Order("127.0.0.1", account)
        orderDao.save(order)

        val orderLine = OrderLine(process.listingPackage.name + " Listing #" + listing.id, process.listingPackage.fee)
        orderLine.vatRate = listingsPluginDao.get.vatRate
        orderLine.order = order
        order.lines.add(orderLine)

        val comment = OrderComment(order,
            "This order was created for <a href='/backoffice/object/" + listing.id + "'>Listing #" + listing
              .id + "</a>")
        order.comments.add(comment)

        orderDao.save(order)
        order
    }

}
