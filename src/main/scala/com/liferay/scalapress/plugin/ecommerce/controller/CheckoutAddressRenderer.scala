package com.liferay.scalapress.plugin.ecommerce.controller

import com.liferay.scalapress.plugin.ecommerce.domain.Basket
import org.springframework.validation.Errors

/** @author Stephen Samuel */
object CheckoutAddressRenderer {

    def renderDeliveryAddress(basket: Basket, errors: Errors) =

        <div id="checkout-addresses">
            <form method="POST" class="form-horizontal">

                {CheckoutWizardRenderer
              .render(CheckoutWizardRenderer.AddressStage)}<legend>Your Details</legend>{BootstrapHelpers
              .controlGroup("accountName", "Account Name", basket.accountName, errors)}{BootstrapHelpers
              .controlGroup("accountEmail",
                "Account Email",
                basket.accountEmail,
                errors)}<legend>Billing Address</legend>{BootstrapHelpers
              .controlGroup("billingAddress.name", "Contact Name", basket.billingAddress.name, errors)}{BootstrapHelpers
              .controlGroup("billingAddress.company",
                "Company",
                basket.billingAddress.company,
                errors)}{BootstrapHelpers
              .controlGroup("billingAddress.address1",
                "Address 1",
                basket.billingAddress.address1,
                errors)}{BootstrapHelpers
              .controlGroup("billingAddress.address2",
                "Address 2",
                basket.billingAddress.address2,
                errors)}{BootstrapHelpers
              .controlGroup("billingAddress.town", "City", basket.billingAddress.town, errors)}{BootstrapHelpers
              .controlGroup("billingAddress.postcode",
                "Postcode",
                basket.billingAddress.postcode,
                errors)}<div class="control-group">
                <label class="control-label" for="country">Country</label>
                <div class="controls">
                    <select name="country" class="input-large">
                        {CountrySelectOptions.render}
                    </select>
                </div>
            </div>{BootstrapHelpers
              .controlGroup("billingAddress.telephone",
                "Telephone",
                basket.billingAddress.telephone,
                errors)}<legend>Delivery Address</legend>
                <label class="checkbox">
                    <input type="checkbox" name="useBillingAddress"/>
                    Use billing address for delivery.
                </label>
                {BootstrapHelpers
              .controlGroup("deliveryAddress.name", "Contact Name",
                basket.deliveryAddress.name, errors)}{BootstrapHelpers
              .controlGroup("deliveryAddress.company", "Company",
                basket.deliveryAddress.company, errors)}{BootstrapHelpers
              .controlGroup("deliveryAddress.address1", "Address 1",
                basket.deliveryAddress.address1, errors)}{BootstrapHelpers
              .controlGroup("deliveryAddress.address2", "Address 2",
                basket.deliveryAddress.address2, errors)}{BootstrapHelpers
              .controlGroup("deliveryAddress.town", "City",
                basket.deliveryAddress.town, errors)}{BootstrapHelpers
              .controlGroup("deliveryAddress.postcode",
                "Postcode", basket.deliveryAddress.postcode, errors)}<div class="control-group">
                <label class="control-label" for="country">Country</label>
                <div class="controls">
                    <select name="deliveryAddress.country" class="input-large">
                        {CountrySelectOptions.render}
                    </select>
                </div>
            </div>{BootstrapHelpers
              .controlGroup("deliveryAddress.telephone",
                "Telephone",
                basket.deliveryAddress.telephone,
                errors)}<div class="control-group">
                <label class="control-label" for="instructions">Delivery Instructions</label>
                <div class="controls">
                    <textarea rows="3" name="basket.deliveryAddress.instructions" class="input-xlarge">
                        {basket.deliveryAddress.instructions}
                    </textarea>
                </div>
            </div>

                <button type="submit" class="btn btn-primary">Continue</button>

            </form>
        </div>
}
