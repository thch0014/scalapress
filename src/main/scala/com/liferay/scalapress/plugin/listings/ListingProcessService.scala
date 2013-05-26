package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.listings.domain.ListingProcess
import com.liferay.scalapress.{ScalapressContext, Logging}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import com.liferay.scalapress.obj.Obj

/** @author Stephen Samuel
  *
  *         This service will complete a listing process
  *
  * */
@Component
class ListingProcessService extends Logging {

    @Autowired var context: ScalapressContext = _
    @Autowired var listingProcessDao: ListingProcessDao = _
    @Autowired var listingsPluginDao: ListingsPluginDao = _

    def build(process: ListingProcess): Obj = {
        logger.info("Building listing for process [{}]", process)

        val account = context.objectDao.find(process.accountId.toLong)
        val listing = _listing(account, process)
        logger.debug("Created listing [{}]", listing.id)

        listing
    }

    // build the listing object
    def _listing(account: Obj, process: ListingProcess) = {
        val obj = new ListingProcessObjectBuilder(context).build(process)
        obj.account = account
        context.objectDao.save(obj)
        obj
    }
}
