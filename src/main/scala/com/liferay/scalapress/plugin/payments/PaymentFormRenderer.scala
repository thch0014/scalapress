package com.liferay.scalapress.plugin.payments

import com.liferay.scalapress.ScalapressContext
import xml.Elem

/** @author Stephen Samuel */
object PaymentFormRenderer {

    def renderPaymentForm(purchase: Purchase, context: ScalapressContext, domain: String): Elem = {

        val payments = context.paymentPluginDao.enabled
        val forms = payments.map(plugin => {

            val buttonText = "Pay with " + plugin.name
            val params = plugin.processor.params(domain, purchase)
            val paramInputs = params.map(arg => <input type="hidden" name={arg._1} value={arg._2}/>)

            <form method="POST" action={plugin.processor.paymentUrl}>
                {paramInputs}<button type="submit" class="btn btn-primary">
                {buttonText}
            </button>
            </form>

        })

        <div id="payment-form">
            <legend>
                Proceed with Payment
            </legend>{forms}
        </div>
    }
}
