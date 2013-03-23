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

    override def tags = Array("basket")
}

object BasketTotalTag extends ScalapressTag with TagBuilder {

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

    override def tags = Array("basket_lines")
}

object BasketLineCountTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        val text = request.basket.lines.size().toString
        Some(build(text, params))
    }

    override def tags = Array("basket_lines_count")
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

    override def tags = Array("basket_line_qty")
}

object BasketDeliveryChargeTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        Option(request.basket.deliveryOption).map(d => {
            val textFormatted = "&pound;%1.2f".format(d.chargeIncVat / 100.0)
            build(textFormatted, params)
        })
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

    override def tags = Array("basket_line_price")
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

    override def tags = Array("basket_line_total")
}

object BasketLineStockTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.line.map(line => build(line.obj.stock.toString, params))
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