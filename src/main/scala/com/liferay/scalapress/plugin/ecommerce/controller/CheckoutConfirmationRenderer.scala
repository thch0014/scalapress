package com.liferay.scalapress.plugin.ecommerce.controller

import com.liferay.scalapress.plugin.ecommerce.domain.{Address, BasketLine, Basket}
import com.liferay.scalapress.plugin.payments.sagepayform.{SagepayFormService, SagepayFormPlugin}
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
object CheckoutConfirmationRenderer {

    def renderConfirmationPage(basket: Basket, plugin: SagepayFormPlugin, domain: String) = {
        val totals = "<legend>Basket Details</legend>" + renderBasket(basket)
        val payments = renderPaymentForm(basket, plugin, domain)
        val delivery = "<div><legend>Delivery Address</legend>" + _renderAddress(basket
          .deliveryAddress) + "<br/><br/></div>"
        val billing = "<div><legend>Billing Address</legend>" + _renderAddress(basket
          .billingAddress) + "<br/><br/></div>"

        "<div id='checkout-confirmation'>" +
          CheckoutWizardRenderer.render(CheckoutWizardRenderer.ConfirmationStage) +
          totals + billing + delivery + payments + "</div>"
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

    private def _renderAddress(address: Address) = {
        <div class="address">
            <div>
                {address.name}
            </div>
            <div>
                {address.company}
            </div>
            <div>
                {address.address1}
            </div>
            <div>
                {address.address2}
            </div>
            <div>
                {address.town}
            </div>
            <div>
                {address.postcode}
            </div>
            <div>
                {address.country}
            </div>
        </div>
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

    private def _renderTotals(basket: Basket) = {

        val subtotal = " £%1.2f".format(basket.subtotal / 100.0)
        val vat = " £%1.2f".format(basket.vat / 100.0)
        val total = " £%1.2f".format(basket.total / 100.0)

        <tr>
            <td></td>
            <td></td>
            <th>Subtotal</th>
            <th>
                {subtotal}
            </th>
        </tr>
          <tr>
              <td></td>
              <td></td>
              <th>Vat</th>
              <th>
                  {vat}
              </th>
          </tr>
          <tr>
              <td></td>
              <td></td>
              <th>Total</th>
              <th>
                  {total}
              </th>
          </tr>
    }

    private def renderBasket(basket: Basket) = {

        <table id="checkout-confirmation-basket" class="table table-striped table-condensed">
            <tr>
                <th>Item</th>
                <th>Qty</th>
                <th>Price</th>
                <th>Total</th>
            </tr>{renderBasketLines(basket.lines.asScala)}{renderDeliveryLine(basket)}{_renderTotals(basket)}
        </table>
    }

    private def renderPaymentForm(basket: Basket, plugin: SagepayFormPlugin, domain: String) = {

        val params = SagepayFormService.params(basket, plugin, domain)
        val termsError = ""
        val paramInputs = params.map(e => <input type="hidden" name={e._1} value={e._2}/>)

        <div id="sagepay-form">
            <legend>Confirm and Pay</legend>
            <form method="POST" action={SagepayFormService.LiveUrl}>
                {paramInputs}<label class="checkbox">
                <input type="checkbox" name="termsAccepted"/>
                Accept
                <a href="/checkout/terms">Terms and Conditions</a>
            </label>{termsError}<label class="checkbox">
                <input type="checkbox" name="newsletterOptin"/>
                Please send me details of your special offers. You can opt out at any time.
            </label>
                <br/> <button type="submit" class="btn btn-primary">Proceed to Payment</button>
            </form>
        </div>
    }
}
