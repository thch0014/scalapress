package com.liferay.scalapress.service.theme.tag.ecommerce

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.controller.admin.UrlResolver

/** @author Stephen Samuel */
object BasketLinkTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val link = UrlResolver.basket
        val text = params.get("text").getOrElse("basket")
        Some(buildLink(link, text, params))
    }
}

object BasketTotalTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val text = request.basket.total
        Some(build(text, params))
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

