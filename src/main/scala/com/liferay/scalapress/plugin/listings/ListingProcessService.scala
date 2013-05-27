package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.listings.domain.ListingProcess
import com.liferay.scalapress.{ScalapressContext, Logging}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.obj.Obj
import scala.collection.JavaConverters._
import com.liferay.scalapress.plugin.listings.email.ListingAdminNotificationService

/** @author Stephen Samuel
  *
  *         This service will complete a listing process
  *
  * */
@Component
class ListingProcessService extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var listingProcessDao: ListingProcessDao = _
    @Autowired var listingAdminNotificationService: ListingAdminNotificationService = _

    def process(process: ListingProcess): Obj = {
        logger.debug("Building listing for process [{}]", process)

        val account = context.objectDao.find(process.accountId.toLong)
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
        logger.debug("Process completed - removing from database")
        listingProcessDao.remove(process)
    }

    // build the listing object
    def _listing(account: Obj, process: ListingProcess) = {
        val obj = new ListingProcessObjectBuilder(context).build(process)
        obj.account = account
        context.objectDao.save(obj)
        obj
    }
}
