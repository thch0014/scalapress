package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.payments.paypal.standard.{PaypalStandardProcessor, PaypalStandardPlugin}
import com.liferay.scalapress.plugin.listings.{ListingProcess}

/** @author Stephen Samuel */
object ListingPaymentRenderer {

    def renderPaypalForm(process: ListingProcess, plugin: PaypalStandardPlugin, domain: String) = {

        val params = PaypalStandardProcessor.params(plugin, domain, new ListingProcessPaymentWrapper(process))
        val paramInputs = params.map(e => <input type="hidden" name={e._1} value={e._2}/>)

        <div id="listing-process-payment">
            <legend>
                Payment
            </legend>
            <form method="POST" action={PaypalStandardProcessor.Production}>
                {paramInputs}<br/> <button type="submit" class="btn btn-primary">
                Proceed to Payment
            </button>
            </form>
        </div>
    }
}
