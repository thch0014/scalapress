package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.listings.ListingProcess
import com.liferay.scalapress.ScalapressContext
import xml.Elem

/** @author Stephen Samuel */
object ListingPaymentRenderer {

    def renderPaymentForms(process: ListingProcess, context: ScalapressContext, domain: String): Elem = {

        val payments = context.paymentPluginDao.enabled
        val forms = payments.map(plugin => {

            val buttonText = "Pay with " + plugin.name
            val params = plugin.processor.params(domain, new ListingProcessPaymentWrapper(process))

            <form method="POST" action={plugin.processor.paymentUrl}>
                {params}<button type="submit" class="btn btn-primary">
                {buttonText}
            </button>
            </form>

        })

        <div id="listing-process-payment">
            <legend>
                Listing Payment
            </legend>{forms}
        </div>
    }
}
