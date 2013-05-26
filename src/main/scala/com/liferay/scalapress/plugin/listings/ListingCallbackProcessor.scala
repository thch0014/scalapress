package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.listings.domain.ListingProcess
import com.liferay.scalapress.obj.Obj
import com.liferay.scalapress.plugin.ecommerce.domain.{OrderComment, OrderLine, Order}
import com.liferay.scalapress.{ScalapressContext, Logging}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.plugin.listings.email.{ListingCustomerNotificationService, ListingAdminNotificationService}
import com.liferay.scalapress.plugin.payments.{PaymentCallback, Transaction}
import com.liferay.scalapress.plugin.ecommerce.OrderDao
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Component
class ListingCallbackProcessor extends PaymentCallback with Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var orderDao: OrderDao = _
    @Autowired var listingsPluginDao: ListingsPluginDao = _
    @Autowired var listingAdminNotificationService: ListingAdminNotificationService = _
    @Autowired var listingCustomerNotificationService: ListingCustomerNotificationService = _

    override def callback(tx: Transaction, id: String) {
        val process = context.objectDao.find(id.toLong)
        callback(Option(tx), process)
    }

    def callback(tx: Option[Transaction], listing: Obj) {
        logger.debug("Performing listing callback [{}-{}]", listing.id, listing.name)

        if (listing.listingPackage.autoPublish) {
            logger.debug("Auto publising listing [{}-{}]", listing.id, listing.name)
            listing.status = Obj.STATUS_LIVE
            context.objectDao.save(listing)
        }

        if (listing.listingPackage.fee > 0) {

            val order = _order(listing)

            if (tx.isDefined) {
                tx.get.order = order.id.toString
                context.transactionDao.save(tx.get)
                order.payments.add(tx.get)
                orderDao.save(order)
            }
        }

        _emails(listing)
    }

    def _emails(listing: Obj) {

        logger.debug("Sending email to customer")
        listingCustomerNotificationService.send(listing, context)

        logger.debug("Sending email to admin")
        listingAdminNotificationService.notify(listing)
    }

    // empty the listing process
    def _cleanup(process: ListingProcess) {
        process.attributeValues.asScala.foreach(_.listingProcess = null)
        process.attributeValues.clear()
        logger.info("Process completed - removing from database")
        //   listingProcessDao.remove(process)
    }

    // build an order to hold the details of what the customer purchased
    def _order(listing: Obj) = {
        logger.debug("Creating order for the listing")

        val order = Order("127.0.0.1", listing.account)
        order.status = Order.STATUS_PAID
        orderDao.save(order)

        val orderLine = OrderLine(listing.listingPackage.name + " Listing #" + listing.id, listing.listingPackage.fee)
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
