package com.cloudray.scalapress.plugin.listings

import com.cloudray.scalapress.item.Item
import com.cloudray.scalapress.plugin.ecommerce.domain.{OrderComment, OrderLine, Order}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.cloudray.scalapress.plugin.listings.email.ListingCustomerNotificationService
import com.cloudray.scalapress.payments.{Callback, Transaction, PaymentCallback}
import com.cloudray.scalapress.framework.{Logging, ScalapressContext}
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.OrderDao

/** @author Stephen Samuel */
@Component
@Callback("Listing")
class ListingCallbackProcessor extends PaymentCallback with Logging {

  @Autowired var context: ScalapressContext = _
  @Autowired var orderDao: OrderDao = _
  @Autowired var listingsPluginDao: ListingsPluginDao = _
  @Autowired var listingCustomerNotificationService: ListingCustomerNotificationService = _

  override def callback(tx: Transaction, id: String) {
    val process = context.itemDao.find(id.toLong)
    callback(Option(tx), process)
  }

  def callback(tx: Option[Transaction], listing: Item) {
    logger.debug("Performing listing callback [{}-{}]", listing.id, listing.name)

    if (listing.listingPackage.autoPublish) {
      logger.debug("Auto publising listing [{}-{}]", listing.id, listing.name)
      listing.status = Item.STATUS_LIVE
      context.itemDao.save(listing)
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

  def _emails(listing: Item) {
    logger.debug("Sending email to customer")
    listingCustomerNotificationService.send(listing)
  }

  // build an order to hold the details of what the customer purchased
  def _order(listing: Item) = {
    logger.debug("Creating order for the listing")

    val order = Order("127.0.0.1", listing.account)
    order.status = Order.STATUS_PAID
    orderDao.save(order)

    val orderLine = OrderLine(listing.listingPackage.name + " Listing #" + listing.id, listing.listingPackage.fee)
    orderLine.vatRate = listingsPluginDao.get.vatRate
    orderLine.order = order
    order.lines.add(orderLine)

    val comment = OrderComment(order,
      "This order was created for <a href='/backoffice/item/" + listing.id + "'>Listing #" + listing
        .id + "</a>")
    order.comments.add(comment)

    orderDao.save(order)
    order
  }

}
