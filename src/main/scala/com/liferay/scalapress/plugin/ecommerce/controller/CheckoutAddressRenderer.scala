package com.liferay.scalapress.plugin.ecommerce.controller

import com.liferay.scalapress.plugin.ecommerce.domain.{Basket, Address, DeliveryOption}
import org.springframework.validation.Errors
import xml.{Null, Text, Attribute, Elem}

/** @author Stephen Samuel */
object CheckoutAddressRenderer {

    def renderDeliveryOptions(basket: Basket, options: List[DeliveryOption]): Seq[Elem] =
        options
          .filter(_.minPrice < basket.linesSubtotal)
          .filter(d => d.maxPrice == 0 || d.maxPrice > basket.linesSubtotal)
          .map(d => {
            val price = " £%1.2f".format(d.chargeIncVat / 100.0)
            val selected = Option(basket.deliveryOption).map(_.id == d.id).getOrElse(false)
            val input = <input type="radio" name="deliveryOptionId" id="deliveryOptionId" value={d.id.toString}/>
            input % Attribute(None, if (selected) "selected" else "notselected", Text("true"), Null)
            <label class="radio">
                {input}{d.name}&nbsp;{price}
            </label>
        })

    def renderDeliveryAddress(basket: Basket, address: Address, options: List[DeliveryOption], errors: Errors) =
        <div class="checkout-delivery-input">
            <form method="POST" class="form-horizontal">
                <legend>Your Details</legend>

                <div class="control-group">
                    <label class="control-label" for="accountName">Name</label>
                    <div class="controls">
                        <input type="text" name="accountName" class="input-xlarge" placeholder="Your Name" value={address
                      .accountName}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("accountName")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="email">Email</label>
                    <div class="controls">
                        <input type="text" name="accountEmail" class="input-xlarge" placeholder="Your email" value={address
                      .accountEmail}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("accountEmail")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <legend>Delivery Address</legend>

                <div class="control-group">
                    <label class="control-label" for="name">Contact Name</label>
                    <div class="controls">
                        <input type="text" name="name" class="input-xlarge" placeholder="Contact Name" value={address
                      .name}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("name")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>


                <div class="control-group">
                    <label class="control-label" for="company">Company</label>
                    <div class="controls">
                        <input type="text" name="company" class="input-xlarge" placeholder="Company" value={address
                      .company}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("company")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="address1">Address 1</label>
                    <div class="controls">
                        <input type="text" name="address1" class="input-xlarge" placeholder="Name" value={address
                      .address1}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("address1")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="address2">Address 2</label>
                    <div class="controls">
                        <input type="text" name="address2" class="input-xlarge" placeholder="Name" value={address
                      .address2}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("address2")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="town">City</label>
                    <div class="controls">
                        <input type="text" name="town" class="input-large" placeholder="City" value={address.town}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("town")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="name">Postcode</label>
                    <div class="controls">
                        <input type="text" name="postcode" class="input-small" placeholder="Postcode" value={address
                      .postcode}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("postcode")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="country">Country</label>
                    <div class="controls">
                        <select name="country" class="input-large">
                            {CountrySelectOptions.render}
                        </select>
                    </div>
                </div>


                <div class="control-group">
                    <label class="control-label" for="telephone">Telephone</label>
                    <div class="controls">
                        <input type="text" name="telephone" placeholder="Telephone" value={address
                      .telephone}/>
                        <span class="help-inline">
                            {Option(errors.getFieldError("telephone")).map(_.getDefaultMessage).getOrElse("")}
                        </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="instructions">Delivery Instructions</label>
                    <div class="controls">
                        <textarea name="instructions" class="input-large">
                            {address.instructions}
                        </textarea>
                    </div>
                </div>

                <legend>Delivery Option</legend>

                <div class="control-group">
                    <label>
                        Delivery Option
                    </label>{renderDeliveryOptions(basket, options)}
                </div>


                <button type="submit" class="btn btn-primary">Continue</button>
            </form>
        </div>

}
