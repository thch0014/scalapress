package com.cloudray.scalapress.plugin.ecommerce.controller.renderers

import com.cloudray.scalapress.plugin.ecommerce.domain._
import scala.collection.JavaConverters._
import com.cloudray.scalapress.plugin.ecommerce.ShoppingPluginDao
import scala.xml.Unparsed

/** @author Stephen Samuel */
object CheckoutConfirmationRenderer {

  def renderConfirmationPage(basket: Basket,
                             domain: String,
                             shoppingPluginDao: ShoppingPluginDao): String = {

    val totals = "<legend>Basket Details</legend>" + renderBasket(basket)

    val delivery = "<div><legend>Delivery Address</legend>" + _renderAddress(basket
      .deliveryAddress) + "<br/><br/></div>"

    val billing = "<div><legend>Billing Address</legend>" + _renderAddress(basket
      .billingAddress) + "<br/><br/></div>"

    val terms = Option(shoppingPluginDao.get.terms).getOrElse("")
    val termsModal = _termsModal(terms)

    "<div id='checkout-confirmation'>" + totals + billing + delivery + _complete + "</div>" + termsModal
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
          {xml.Unparsed(price)}
        </td>
        <td>
          {xml.Unparsed(total)}
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

  def renderDeliveryLine(basket: Basket) = {

    val name = Option(basket.deliveryOption).map(_.name).orNull
    val price = Option(basket.deliveryOption).map(_.chargeIncVat / 100.0).getOrElse(0.0)
    val priceFormatted = Unparsed(" &pound;%1.2f".format(price))

    <tr>
      <td>
        {name}
      </td>
      <td></td>
      <td>
        {priceFormatted}
      </td>
      <td>
        {priceFormatted}
      </td>
    </tr>
  }

  def _complete = {

    val termsError = ""
    <form method="POST" action="/checkout/confirmation">
      <legend>
        Confirm Order Details
      </legend>
      <label class="checkbox">
        <input type="checkbox" name="termsAccepted"/>
        Accept
        <a href="#termsModal" data-toggle="modal">
          Terms and Conditions
        </a>
      </label>{termsError}<label class="checkbox">
      <input type="checkbox" name="newsletterOptin"/>
      Please send me details of your special offers. You can opt out at any time.
    </label>
      <button type="submit" class="btn btn-default">
        Confirm
      </button>
    </form>
  }

  def _termsModal(text: String) = {
    val content = scala.xml.Unparsed(text)
    <div id="termsModal" class="modal fade hide" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">Terms and Conditions</h3>
      </div>
      <div class="modal-body">
        <p>
          {content}
        </p>
      </div>
      <div class="modal-footer">
        <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">Close</button>
      </div>
    </div>
  }

  def _renderTotals(basket: Basket) = {

    val subtotal = xml.Unparsed(" &pound;%1.2f".format(basket.subtotal / 100.0))
    val vat = xml.Unparsed(" &pound;%1.2f".format(basket.vat / 100.0))
    val total = xml.Unparsed(" &pound;%1.2f".format(basket.total / 100.0))

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

  def renderBasket(basket: Basket) = {

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

}