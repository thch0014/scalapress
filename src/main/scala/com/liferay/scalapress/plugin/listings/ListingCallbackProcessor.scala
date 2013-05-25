package com.liferay.scalapress.plugin.listings

import com.liferay.scalapress.plugin.payments.Transaction
import com.liferay.scalapress.plugin.listings.domain.ListingProcess

/** @author Stephen Samuel */
object ListingCallbackProcessor {

    def callback(tx: Transaction, listingProcess: ListingProcess): Boolean = {

        Option(context.listingProcessDao.find(params.get("custom").getOrElse("0"))) match {
            case None => logger.warn("Could not find listing process session for callback")
            case (Some(process)) =>
                logger.info("Processing process to listing [{}]", process)
                listingProcessService.process(tx, process, req)
        }

        true
    }
}
