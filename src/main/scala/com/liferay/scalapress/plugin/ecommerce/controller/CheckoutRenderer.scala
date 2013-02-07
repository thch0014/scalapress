package com.liferay.scalapress.plugin.ecommerce.controller

/** @author Stephen Samuel */
object CheckoutRenderer {

    def renderPaymentOptions =
        <div class="container">
            <form>
                <button type="submit" class="btn btn-primary">Protx</button>
            </form>
        </div>

    def renderDeliveryAddress =
        <div class="container">
            <form>
                <div class="control-group">
                    <label for="name">
                        Name
                    </label>
                    <input name="name" type=" "/>
                </div>
                <div class="control-group">
                    <label for="company">
                        Company
                    </label>
                    <input name="company" type=" "/>
                </div>
                <div class="control-group">
                    <label for="address1">
                        Address 1
                    </label>
                    <input name="address1" type=" "/>
                </div>
                <div class="control-group">
                    <label for="address2">
                        Address 2
                    </label>
                    <input name="address2" type=" "/>
                </div>
                <div class="control-group">
                    <label for="city">
                        City
                    </label>
                    <input name="city" type=" "/>
                </div>
                <div class="control-group">
                    <label for="postcode">
                        Postcode
                    </label>
                    <input name="postcode" type=" "/>
                </div>
                <div class="control-group">
                    <label for="country">
                        Country
                    </label>
                    <select name="country">
                    </select>
                </div>
                <div class="control-group">
                </div>
                <div class="control-group">
                    <label for="telephone">
                        Telephone
                    </label>
                    <input name="telephone" type=" "/>
                </div>
                <div class="control-group">
                    <label for="instructions">
                        Delivery Instructions
                    </label>
                    <textarea name="instructions"></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Continue</button>
            </form>
        </div>

}
