package com.liferay.scalapress.plugin.listings.controller

import com.liferay.scalapress.plugin.payments.paypal.standard.{PaypalStandardService, PaypalStandardPlugin}
import com.liferay.scalapress.plugin.listings.ListingProcess
import com.liferay.scalapress.plugin.ecommerce.domain.Basket

/** @author Stephen Samuel */
object ListingPaymentRenderer {

    def renderPaypalForm(process: ListingProcess, plugin: PaypalStandardPlugin, domain: String) = {

        val params = PaypalStandardService.params(plugin, domain, new Basket)
        val paramInputs = params.map(e => <input type="hidden" name={e._1} value={e._2}/>)

        <div id="paypal-standard-form">
            <legend>
                Confirm and Pay
            </legend>
            <form method="POST" action={PaypalStandardService.Production}>
                {paramInputs}<br/> <button type="submit" class="btn btn-primary">
                Proceed to Payment
            </button>
            </form>
        </div>
    }
}
