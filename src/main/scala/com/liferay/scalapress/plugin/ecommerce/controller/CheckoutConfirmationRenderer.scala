package com.liferay.scalapress.plugin.ecommerce.controller

import com.liferay.scalapress.plugin.ecommerce.domain.{BasketLine, Basket}
import com.liferay.scalapress.plugin.payments.sagepayform.{SagepayFormService, SagepayFormPlugin}
import com.liferay.scalapress.domain.Obj
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object CheckoutConfirmationRenderer {

    def renderConfirmationPage(basket: Basket, plugin: SagepayFormPlugin, domain: String) = {
        val totals = renderBasket(basket).toString()
        val payments = renderPaymentForm(basket, plugin, domain).toString()
        totals + "\n\n" + payments
    }

    private def renderBasketLines(lines: Seq[BasketLine]) = {
        lines.map(line => {

            val price = " £%1.2f".format(line.obj.sellPriceInc / 100.0)
            val total = " £%1.2f".format(line.total / 100.0)

            <tr>
                <td>
                    {line.obj.name}
                </td>
                <td>
                    {line.qty}
                </td>
                <td>
                    {price}
                </td>
                <td>
                    {total}
                </td>
            </tr>
        })
    }

    private def renderDeliveryLine(basket: Basket) = {

        val price = " £%1.2f".format(basket.deliveryOption.chargeIncVat / 100.0)

        <tr>
            <td>
                {basket.deliveryOption.name}
            </td>
            <td></td>
            <td>
                {price}
            </td>
            <td>
                {price}
            </td>
        </tr>
    }

    private def renderBasket(basket: Basket) = {

        <table id="checkout-confirmation-basket" class="table table-striped table-condensed">
            <tr>
                <th>Item</th>
                <th>Qty</th>
                <th>Price</th>
                <th>Total</th>
            </tr>{renderBasketLines(basket.lines.asScala)}{renderDeliveryLine(basket)}
        </table>
    }

    private def renderPaymentForm(basket: Basket, plugin: SagepayFormPlugin, domain: String) = {

        val params = SagepayFormService.params(basket, plugin, domain)
        val termsError = ""
        val paramInputs = params.map(e => <input type="hidden" name={e._1} value={e._2}/>)

        <div class="checkout-payment-form">
            <form method="POST" action={SagepayFormService.LiveUrl}>
                <label class="control-label" for="name">Accept
                    <a href="/checkout/terms">Terms and Conditions</a>
                </label>
                <div class="controls">
                    <input type="checkbox" name="termsAccepted"/>{termsError}
                </div>{paramInputs}<button type="submit" class="btn btn-primary">Proceed to Payment</button>
            </form>
        </div>
    }
}
