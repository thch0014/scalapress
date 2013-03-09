package com.liferay.scalapress.controller.web.ecommerce

import com.liferay.scalapress.domain.ecommerce.Basket

/** @author Stephen Samuel */
object BasketRenderer {

    def render(basket: Basket) = "Basket id : " + basket.id
}
