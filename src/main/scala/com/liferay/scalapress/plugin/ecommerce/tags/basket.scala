package com.liferay.scalapress.plugin.ecommerce.tags

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.controller.admin.UrlResolver
import com.liferay.scalapress.service.theme.MarkupRenderer
import scala.collection.JavaConverters._
import com.liferay.scalapress.service.FriendlyUrlGenerator

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

object BasketLinesTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        // we need to be inside a basket context
        val lines = request.basket.lines
        Option(context.shoppingPluginDao.get.basketLineMarkup) match {
            case None => None
            case Some(m) =>
                val render = MarkupRenderer.render(lines.asScala, m, request)
                Some(render)
        }
    }

    override def tags = Array("basket_lines")
}

object BasketLineQtyTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.line.map(line => "<input type='text' size='4' name='qty" + line.id + " value='" + line.qty + "/>")
    }

    override def tags = Array("basket_line_qty")
}

object BasketDeliveryChargeTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        Option(request.basket.deliveryOption).map(_.chargeIncVat.toString)
    }
    override def tags = Array("basket_delivery_charge")
}

object BasketDeliveryDescTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        Option(request.basket.deliveryOption).map(_.name)
    }
    override def tags = Array("basket_delivery_desc")
}

object BasketLineItemTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.line.map(line => {
            params.contains("link") match {
                case false => line.obj.name
                case true => FriendlyUrlGenerator.friendlyUrl(line.obj)
            }
        })
    }

    override def tags = Array("basket_line_item")
}

object BasketLinePriceTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        if (params.contains("ex"))
            request.line.map(_.obj.sellPrice.toString)
        else if (params.contains("vat"))
            request.line.map(_.obj.vat.toString)
        else
            request.line.map(_.obj.sellPriceInc.toString)
    }

    override def tags = Array("basket_line_price")
}

object BasketLineTotalTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        if (params.contains("ex"))
            request.line.map(_.subtotal.toString)
        else if (params.contains("vat"))
            request.line.map(_.vat.toString)
        else
            request.line.map(_.total.toString)
    }

    override def tags = Array("basket_line_total")
}

object BasketLineStockTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.line.map(_.obj.stock.toString)
    }

    override def tags = Array("basket_line_stock")
}

object BasketRemoveItemTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        val text = params.get("text").getOrElse("Remove")
        val href = "/basket/remove/" + request.line.map(_.id.toString).getOrElse("")
        val link = buildLink(href, text, params)
        Some(link)
    }

    override def tags = Array("basket_line_remove")
}

object CheckoutTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        val link = UrlResolver.checkout
        val text = params.get("text").getOrElse("Checkout")
        Some(buildLink(link, text, params))
    }

    override def tags = Array("checkout")
}