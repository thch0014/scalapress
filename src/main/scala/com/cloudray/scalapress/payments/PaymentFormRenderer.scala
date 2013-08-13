package com.cloudray.scalapress.payments

import com.cloudray.scalapress.ScalapressContext
import xml.Elem
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Component
class PaymentFormRenderer {

  @Autowired var context: ScalapressContext = _

  def renderPaymentForm(purchase: Purchase): Elem = {

    val payments = context.paymentPluginDao.enabled
    val forms = payments.map(plugin => {

      val buttonText = "Pay with " + plugin.name
      val params = plugin.processor.params(context.installationDao.get.domain, purchase)
      val paramInputs = params.map(arg => <input type="hidden" name={arg._1} value={arg._2}/>)

      <form method="POST" action={plugin.processor.paymentUrl}>
        {paramInputs}<button type="submit" class="btn">
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
