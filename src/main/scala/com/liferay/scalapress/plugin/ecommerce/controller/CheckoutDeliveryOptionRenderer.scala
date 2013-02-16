package com.liferay.scalapress.plugin.ecommerce.controller

import com.liferay.scalapress.plugin.ecommerce.domain.{DeliveryOption, Basket}
import xml.Elem
import org.springframework.validation.Errors

/** @author Stephen Samuel */
object CheckoutDeliveryOptionRenderer {

    def renderDeliveryOptions(basket: Basket, options: List[DeliveryOption], errors: Errors): Elem = {
        val r = options
          .filter(_.minPrice <= basket.linesSubtotal)
          .filter(d => d.maxPrice == 0 || d.maxPrice >= basket.linesSubtotal)
          .map(d => {

            val price = " £%1.2f".format(d.chargeIncVat / 100.0)
            val checked = Option(basket.deliveryOption).exists(_.id == d.id)

            <label class="radio">
                <input type="radio" name="deliveryOptionId" id="deliveryOptionId"
                       value={d.id.toString} checked={if (checked) "true" else null}/>{d.name}&nbsp;{price}
            </label>
        })

        val error = Option(errors.getFieldError("deliveryOptionId"))
          .map(arg =>
            <div class="alert alert-error">
                {arg}
            </div>)
          .getOrElse(<span/>)

        <div id="checkout-delivery">
            {error}<form method="POST" action="/checkout/delivery" class="form-horizontal">
            {CheckoutWizardRenderer
              .render(CheckoutWizardRenderer
              .DeliveryStage)}<legend>Delivery Method</legend>{r}<br/> <button type="submit" class="btn btn-primary">Continue</button>
        </form>
        </div>
    }
}