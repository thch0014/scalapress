package com.liferay.scalapress.plugin.ecommerce.controller

import com.liferay.scalapress.plugin.ecommerce.domain.{Address, BasketLine, Basket}
import com.liferay.scalapress.plugin.payments.sagepayform.{SagepayFormService, SagepayFormPlugin}
import scala.collection.JavaConverters._
import com.liferay.scalapress.plugin.ecommerce.ShoppingPlugin

/** @author Stephen Samuel */
object CheckoutConfirmationRenderer {

    def renderConfirmationPage(basket: Basket,
                               sagepayFormPlugin: SagepayFormPlugin,
                               shoppingPlugin: ShoppingPlugin,
                               domain: String): String = {

        val wizard = CheckoutWizardRenderer.render(CheckoutWizardRenderer.ConfirmationStage)
        val totals = "<legend>Basket Details</legend>" + renderBasket(basket)
        val payments = renderPaymentForm(basket, sagepayFormPlugin, domain)
        val delivery = "<div><legend>Delivery Address</legend>" + _renderAddress(basket
          .deliveryAddress) + "<br/><br/></div>"
        val billing = "<div><legend>Billing Address</legend>" + _renderAddress(basket
          .billingAddress) + "<br/><br/></div>"
        val terms = _terms(shoppingPlugin.terms)

        "<div id='checkout-confirmation'>" + wizard + totals + billing + delivery + payments + "</div>" + terms
    }

    private def renderBasketLines(lines: Seq[BasketLine]) = {
        lines.map(line => {

            val price = " &pound;%1.2f".format(line.obj.sellPriceInc / 100.0)
            val total = " &pound;%1.2f".format(line.total / 100.0)

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

        val price = " &pound;%1.2f".format(basket.deliveryOption.chargeIncVat / 100.0)

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

    private def _terms(text: String) = {
        <div id="termsModal" class="modal fade hide" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="myModalLabel">Terms and Conditions</h3>
            </div>
            <div class="modal-body">
                <p>
                    {text}
                </p>
            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
            </div>
        </div>
    }

    private def _renderTotals(basket: Basket) = {

        val subtotal = " &pound;%1.2f".format(basket.subtotal / 100.0)
        val vat = " &pound;%1.2f".format(basket.vat / 100.0)
        val total = " &pound;%1.2f".format(basket.total / 100.0)

        <tr>
            <td></td>
            <td></td>
            <th>
                Subtotal
            </th>
            <th>
                {subtotal}
            </th>
        </tr>
          <tr>
              <td></td>
              <td></td>
              <th>
                  Vat
              </th>
              <th>
                  {vat}
              </th>
          </tr>
          <tr>
              <td></td>
              <td></td>
              <th>
                  Total
              </th>
              <th>
                  {total}
              </th>
          </tr>
    }

    private def renderBasket(basket: Basket) = {

        <table id="checkout-confirmation-basket" class="table table-striped table-condensed">
            <tr>
                <th>
                    Item
                </th>
                <th>
                    Qty
                </th>
                <th>
                    Price
                </th>
                <th>
                    Total
                </th>
            </tr>{renderBasketLines(basket.lines.asScala)}{renderDeliveryLine(basket)}{_renderTotals(basket)}
        </table>
    }

    private def renderPaymentForm(basket: Basket, plugin: SagepayFormPlugin, domain: String) = {

        val params = SagepayFormService.params(basket, plugin, domain)
        val termsError = ""
        val paramInputs = params.map(e => <input type="hidden" name={e._1} value={e._2}/>)

        <div id="sagepay-form">
            <legend>
                Confirm and Pay
            </legend>
            <form method="POST" action={SagepayFormService.LiveUrl}>
                {paramInputs}<label class="checkbox">
                <input type="checkbox" name="termsAccepted"/>
                Accept
                <a href="#termsModal" data-toggle="modal">Terms and Conditions</a>
            </label>{termsError}<label class="checkbox">
                <input type="checkbox" name="newsletterOptin"/>
                Please send me details of your special offers. You can opt out at any time.
            </label>
                <br/> <button type="submit" class="btn btn-primary">
                Proceed to Payment
            </button>
            </form>
        </div>
    }
}
