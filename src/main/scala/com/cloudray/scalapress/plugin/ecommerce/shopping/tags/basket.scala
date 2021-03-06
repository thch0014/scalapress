package com.cloudray.scalapress.plugin.ecommerce.tags

import scala.collection.JavaConverters._
import com.cloudray.scalapress.theme.MarkupRenderer
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.item.StockMethod
import com.cloudray.scalapress.framework.{UrlGenerator, ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("basket")
class BasketLinkTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val link = "/basket"
    val text = params.get("text").getOrElse("Basket")
    Some(buildLink(link, text, params))
  }
}

@Tag("basket_total")
class BasketTotalTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
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

@Tag("basket_lines_total")
class BasketLinesTotalTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
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

@Tag("addtobasket")
class AddToBasketTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.item match {
      case None => None
      case Some(obj) =>
        obj.available || obj.backorders || request.shoppingPlugin.stockMethod == StockMethod.Off match {
          case true =>
            val text = params.get("text").getOrElse("Add to basket")
            val href = "/basket/add/" + obj.id
            val link = buildLink(href, text, params)
            Some(link)
          case false => None
        }
    }
  }
}

@Tag("basket_lines")
class BasketLinesTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    // we need to be inside a basket context
    val lines = request.basket.lines
    Option(request.shoppingPlugin.basketLineMarkup) match {
      case None => None
      case Some(m) =>
        val render = MarkupRenderer.render(lines.asScala, m, request)
        Some(render)
    }
  }
}

@Tag("basket_lines_count")
class BasketLineCountTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val text = request.basket.lines.size().toString
    Some(build(text, params))
  }
}

@Tag("basket_line_desc")
class BasketLineDescTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = request.line.map(_.description)
}

@Tag("basket_line_variation")
class BasketLineVariationTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] =
    request.line.flatMap(line => Option(line.variation)).map(_.name)
}

@Tag("basket_line_qty")
class BasketLineQtyTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.line.map(line => {
      val name = "qty" + line.id
        <input type="number" min="1" max="100" step="1" class="input-mini" name={name} value={line.qty.toString}/>
        .toString()
    })
  }
}

@Tag("basket_delivery_charge")
class BasketDeliveryChargeTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    Option(request.basket.deliveryOption).map(d => {
      val textFormatted = "&pound;%1.2f".format(d.chargeIncVat / 100.0)
      build(textFormatted, params)
    })
  }
}

@Tag("basket_delivery_desc")
class BasketDeliveryDescTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    Option(request.basket.deliveryOption).map(_.name)
  }
}

@Tag("basket_line_item")
class BasketLineItemTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.line.map(line => {
      params.contains("link") match {
        case false => line.obj.name
        case true => UrlGenerator.url(line.obj)
      }
    })
  }
}

@Tag("basket_line_price")
class BasketLinePriceTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.line.map(line => {
      val price =
        if (params.contains("ex")) line.price
        else if (params.contains("vat") && request.installation.vatEnabled) line.priceVat
        else if (params.contains("vat")) 0
        else if (request.installation.vatEnabled) line.priceInc
        else line.price
      "&pound;%1.2f".format(price / 100.0)
    })
  }
}

@Tag("basket_line_total")
class BasketLineTotalTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    request.line.map(line => {
      val total =
        if (params.contains("ex")) line.subtotal
        else if (params.contains("vat") && request.installation.vatEnabled) line.vat
        else if (params.contains("vat")) 0
        else if (request.installation.vatEnabled) line.total
        else line.subtotal
      "&pound;%1.2f".format(total / 100.0)
    })
  }
}

@Tag("basket_line_stock")
class BasketLineStockTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.line.flatMap(line => Option(line.obj)).map(obj => build(obj.stock.toString, params))
  }
}

@Tag("basket_line_remove")
class BasketRemoveItemTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val text = params.get("text").getOrElse("Remove")
    val href = "/basket/remove/" + request.line.map(_.id.toString).getOrElse("")
    val link = buildLink(href, text, params)
    Some(link)
  }
}

@Tag("checkout")
class CheckoutTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    val link = "/checkout"
    val text = params.get("text").getOrElse("Checkout")
    Some(buildLink(link, text, params))
  }
}

@Tag("basket_form")
class BasketFormTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.item.map(obj => s"<form method='GET' action='/basket/add/${obj.id}'>")
  }
}