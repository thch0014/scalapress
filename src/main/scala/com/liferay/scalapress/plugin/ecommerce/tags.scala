package com.liferay.scalapress.plugin.ecommerce

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.controller.admin.UrlResolver

/** @author Stephen Samuel */
object BasketLinkTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val link = UrlResolver.basket
        val text = params.get("text").getOrElse("Basket")
        Some(buildLink(link, text, params))
    }

    override def tags = Array("basket")
}

object BasketTotalTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        request.basket match {
            case None => None
            case Some(basket) =>
                val text = basket.total
                Some(build(text, params))
        }
    }
}

object AddToBasketTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        request.obj match {
            case None => None
            case Some(obj) =>
                val label = params.get("label").getOrElse("Add to basket")
                val href = UrlResolver.addToBasket(obj)
                val link = buildLink(href, label, params)
                Some(link)
        }
    }
}

object BasketLinesTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = None

    override def tags = Array("basket_lines")
}

object CheckoutTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val link = UrlResolver.checkout
        val text = params.get("text").getOrElse("Checkout")
        Some(buildLink(link, text, params))
    }

    override def tags = Array("checkout")
}