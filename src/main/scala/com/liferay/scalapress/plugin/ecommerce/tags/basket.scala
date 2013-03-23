package com.liferay.scalapress.plugin.ecommerce.tags

import com.liferay.scalapress.{Tag, FriendlyUrlGenerator, ScalapressContext, ScalapressRequest}
import scala.collection.JavaConverters._
import com.liferay.scalapress.enums.StockMethod
import com.liferay.scalapress.util.mvc.UrlResolver
import com.liferay.scalapress.theme.MarkupRenderer
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel */
@Tag("basket")
class BasketLinkTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val link = UrlResolver.basket
        val text = params.get("text").getOrElse("Basket")
        Some(buildLink(link, text, params))
    }
}

@Tag("basket_total")
class BasketTotalTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val text = if (params.contains("ex"))
            request.basket.subtotal
        else if (params.contains("vat"))
            request.basket.vat
        else
            request.basket.total
        val textFormatted = "&pound;%1.2f".format(text / 100.0)
        Some(build(textFormatted, params))
    }
}

object BasketLinesTotalTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val text = if (params.contains("ex"))
            request.basket.linesSubtotal
        else if (params.contains("vat"))
            request.basket.linesVat
        else
            request.basket.linesTotal
        val textFormatted = "&pound;%1.2f".format(text / 100.0)
        Some(build(textFormatted, params))
    }
}

object AddToBasketTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {

        request.obj match {
            case None => None
            case Some(obj) =>
                obj.available || obj.backorders || context.shoppingPluginDao.get.stockMethod == StockMethod.Off match {
                    case true =>
                        val text = params.get("text").getOrElse("Add to basket")
                        val href = UrlResolver.addToBasket(obj)
                        val link = buildLink(href, text, params)
                        Some(link)
                    case false => None
                }
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
}

object BasketLineCountTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        val text = request.basket.lines.size().toString
        Some(build(text, params))
    }

}

object BasketLineQtyTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request
          .line
          .map(line => "<input type='text' class='input-mini' name='qty" +
          line.id + "' value='" + line.qty + "'/>")
    }
}

@Tag("basket_delivery_charge")
class BasketDeliveryChargeTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        Option(request.basket.deliveryOption).map(d => {
            val textFormatted = "&pound;%1.2f".format(d.chargeIncVat / 100.0)
            build(textFormatted, params)
        })
    }
}

@Tag("basket_delivery_desc")
class BasketDeliveryDescTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        Option(request.basket.deliveryOption).map(_.name)
    }
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
}

object BasketLinePriceTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        val text = if (params.contains("ex"))
            request.line.map(_.obj.sellPrice)
        else if (params.contains("vat"))
            request.line.map(_.obj.vat)
        else
            request.line.map(_.obj.sellPriceInc)

        text match {
            case None => None
            case Some(price) =>
                val textFormatted = "&pound;%1.2f".format(price / 100.0)
                Some(textFormatted)
        }
    }
}

object BasketLineTotalTag extends ScalapressTag {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        val total = if (params.contains("ex"))
            request.line.map(_.subtotal)
        else if (params.contains("vat"))
            request.line.map(_.vat)
        else
            request.line.map(_.total)

        total match {
            case None => None
            case Some(price) =>
                val textFormatted = "&pound;%1.2f".format(price / 100.0)
                Some(textFormatted)
        }
    }
}

object BasketLineStockTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.line.map(line => build(line.obj.stock.toString, params))
    }
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
}

@Tag("checkout")
class CheckoutTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        val link = UrlResolver.checkout
        val text = params.get("text").getOrElse("Checkout")
        Some(buildLink(link, text, params))
    }
}