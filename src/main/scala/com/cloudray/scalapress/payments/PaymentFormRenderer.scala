package com.cloudray.scalapress.payments

import com.cloudray.scalapress.ScalapressContext
import scala.xml.{Unparsed, Elem}
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Autowired
import com.cloudray.scalapress.util.Scalate

/** @author Stephen Samuel */
@Component
@Autowired
class PaymentFormRenderer(context: ScalapressContext) {

  val PAY_WITH_CARD = "Pay with debit or credit card"

  def renderPaymentForm(purchase: Purchase): Elem = renderPaymentForm(purchase, false)
  def renderPaymentForm(purchase: Purchase, vouchersEnabled: Boolean): Elem = {

    val payments = context.paymentPluginDao.enabled
    val forms = payments.map(plugin => {

      val buttonText = if (payments.size == 1) PAY_WITH_CARD else s"Pay with ${plugin.name}"
      val params = plugin.processor.params(context.installationDao.get.domain, purchase)
      val paramInputs = params.map(arg => <input type="hidden" name={arg._1} value={arg._2}/>)

      <form method="POST" action={plugin.processor.paymentUrl}>
        {paramInputs}<button type="submit" class="btn btn-default">
        {buttonText}
      </button>
      </form>

    })

    val v = if (vouchersEnabled) Unparsed(voucher(purchase)) else Unparsed("")

    <div id="payment-form">
      <div class="payments">
        <legend>
          Proceed with Payment
        </legend>{forms}
      </div>{v}
    </div>
  }

  def voucher(purchase: Purchase) = Scalate.layout(
    "/com/cloudray/scalapress/plugin/listings/voucher-input.ssp",
    Map("code" -> purchase.voucher.getOrElse(""))
  )
}
