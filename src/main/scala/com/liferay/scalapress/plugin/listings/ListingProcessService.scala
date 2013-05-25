package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.listings.domain.{ListingsPlugin, ListingProcess}
import com.liferay.scalapress.{ScalapressContext, Logging}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.obj.Obj
import scala.collection.JavaConverters._
import com.liferay.scalapress.plugin.ecommerce.domain.{OrderComment, OrderLine, Order}
import com.liferay.scalapress.plugin.listings.email.{ListingCustomerNotificationService, ListingAdminNotificationService}

/** @author Stephen Samuel
  *
  *         This service will complete a listing process
  *
  **/
@Component
class ListingProcessService extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var listingProcessDao: ListingProcessDao = _
    @Autowired var listingsPluginDao: ListingsPluginDao = _
    @Autowired var listingAdminNotificationService: ListingAdminNotificationService = _
    @Autowired var listingCustomerNotificationService: ListingCustomerNotificationService = _

    def complete(process: ListingProcess): Obj = {
        logger.info("Building listing for process [{}]", process)
        val plugin = listingsPluginDao.get

        val account = context.objectDao.find(process.accountId.toLong)
        val obj = _listing(account, process)
        logger.debug("Created listing [{}]", obj.id)

        logger.debug("Sending email to customer")
        listingCustomerNotificationService.send(obj, context)

        logger.debug("Sending email to admin")
        listingAdminNotificationService.notify(obj, process)

        if (process.listingPackage.fee > 0) {
            _order(account, obj, process, plugin)
        }

        _cleanup(process)
        obj
    }

    // build an order to hold the details of what the customer purchased
    def _order(account: Obj, listing: Obj, process: ListingProcess, plugin: ListingsPlugin) = {
        logger.debug("Creating order for the listing")

        val order = Order("127.0.0.1", account)
        context.orderDao.save(order)

        val orderLine = OrderLine(process.listingPackage.name + " Listing #" + listing.id, process.listingPackage.fee)
        orderLine.vatRate = plugin.vatRate
        orderLine.order = order
        order.lines.add(orderLine)

        val comment = OrderComment(order,
            "This order was created for <a href='/backoffice/object/" + listing.id + "'>Listing #" + listing
              .id + "</a>")
        order.comments.add(comment)

        context.orderDao.save(order)
        order
    }

    // build the listing object
    def _listing(account: Obj, process: ListingProcess) = {
        val obj = new ListingProcessObjectBuilder(context).build(process)
        obj.account = account
        context.objectDao.save(obj)
        obj
    }

    // empty the listing process
    def _cleanup(process: ListingProcess) {
        process.attributeValues.asScala.foreach(_.listingProcess = null)
        process.attributeValues.clear()
        logger.info("Process completed - removing from database")
        listingProcessDao.remove(process)
    }
}
