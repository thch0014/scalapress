package com.liferay.scalapress.plugin.listings.controller.renderer

import com.liferay.scalapress.plugin.listings.ListingProcessPurchase
import com.liferay.scalapress.ScalapressContext
import xml.Elem
import com.liferay.scalapress.plugin.listings.domain.ListingProcess

/** @author Stephen Samuel */
object ListingPaymentRenderer {

    def renderPaymentForms(process: ListingProcess, context: ScalapressContext, domain: String): Elem = {

        val payments = context.paymentPluginDao.enabled
        val forms = payments.map(plugin => {

            val buttonText = "Pay with " + plugin.name
            val params = plugin.processor.params(domain, new ListingProcessPurchase(process))
            val paramInputs = params.map(arg => <input type="hidden" name={arg._1} value={arg._2}/>)

            <form method="POST" action={plugin.processor.paymentUrl}>
                {paramInputs}<button type="submit" class="btn btn-primary">
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
