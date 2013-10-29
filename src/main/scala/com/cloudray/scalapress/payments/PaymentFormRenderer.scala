package com.cloudray.scalapress.payments

import com.cloudray.scalapress.ScalapressContext
import scala.xml.Elem
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel */
@Component
@Autowired
class PaymentFormRenderer(context: ScalapressContext) {

  val PAY_WITH_CARD = "Pay £%.2f with debit or credit card"
  val PAY_WITH_PROCESSOR = "Pay £%.2f with %s"

  def renderPaymentForm(purchase: Purchase): Elem = {

    val payments = context.paymentPluginDao.enabled
    val forms = payments.map(plugin => {

      val buttonText = if (payments.size == 1) PAY_WITH_CARD.format(purchase.total / 100d)
      else PAY_WITH_PROCESSOR.format(purchase.total / 100d, plugin.name)
      val params = plugin.processor.params(context.installationDao.get.domain, purchase)
      val paramInputs = params.map(arg => <input type="hidden" name={arg._1} value={arg._2}/>)

      <form method="POST" action={plugin.processor.paymentUrl}>
        {paramInputs}<button type="submit" class="btn btn-default">
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
