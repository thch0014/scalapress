package com.cloudray.scalapress.plugin.listings

import com.cloudray.scalapress.plugin.listings.domain.ListingProcess
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.cloudray.scalapress.item.Item
import scala.collection.JavaConverters._
import com.cloudray.scalapress.plugin.listings.email.ListingAdminNotificationService
import com.cloudray.scalapress.account.Account
import com.cloudray.scalapress.framework.{Logging, ScalapressContext}

/** @author Stephen Samuel
  *
  *         This service will complete a listing process
  *
  **/
@Component
class ListingProcessService extends Logging {

  @Autowired var context: ScalapressContext = _
  @Autowired var listingProcessDao: ListingProcessDao = _
  @Autowired var listingAdminNotificationService: ListingAdminNotificationService = _

  def process(process: ListingProcess): Item = {
    logger.debug("Building listing for process [{}]", process)

    val account = context.accountDao.find(process.accountId.toLong)
    val listing = _listing(account, process)
    logger.debug("Created listing [{}]", listing.id)

    logger.debug("Sending email to admin")
    listingAdminNotificationService.notify(listing)

    listing
  }

  // delete the listing process
  def cleanup(process: ListingProcess) {
    process.attributeValues.asScala.foreach(_.listingProcess = null)
    process.attributeValues.clear()
    process.title = null
    process.content = null
    process.imageKeys = Array()
    process.email = null
    process.listing = null
    process.folders = Array()
    listingProcessDao.save(process)
    logger.debug("Process completed - removing from database")
    listingProcessDao.remove(process)
  }

  // build the listing object
  def _listing(account: Account, process: ListingProcess) = {
    val obj = new ListingProcessItemBuilder(context).build(process)
    obj.account = account
    context.itemDao.save(obj)
    obj
  }
}
