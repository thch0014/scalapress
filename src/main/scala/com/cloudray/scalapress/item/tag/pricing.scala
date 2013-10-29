package com.cloudray.scalapress.item.tag

import com.cloudray.scalapress.{Tag, ScalapressRequest}
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.item.StockMethod

/** @author Stephen Samuel */
object RrpTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.item.flatMap(item => {
      item.rrp match {
        case 0 => None
        case _ =>
          val formatted = "&pound;%1.2f".format(item.rrp / 100.0)
          Some(build(formatted, params))
      }
    })
  }
}

object RrpDiscountTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.item.flatMap(item => {

      val saving = item.rrp - item.sellPriceInc
      saving match {
        case i if i <= 0 => None
        case _ =>
          val textFormatted = "&pound;%1.2f".format(saving / 100.0)
          Some(build(textFormatted, params))
      }
    })
  }
}

object ItemSellPriceTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {
    request.item.map(item => {

      val text = if (params.contains("ex")) item.price
      else if (params.contains("vat") && request.installation.vatEnabled) item.vat
      else if (params.contains("vat")) 0
      else if (request.installation.vatEnabled) item.sellPriceInc
      else item.price

      val textFormatted = "&pound;%1.2f".format(text / 100.0)
      build(textFormatted, params + ("class" -> "price"))

    })
  }
}

@Tag("stock")
class TagStockTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.item.map(item => {
      request.shoppingPlugin.stockMethod match {
        case StockMethod.Automatic => build(item.stock.toString, params)
        case StockMethod.InOut =>
          val stock = if (item.stock > 0) "1" else "0"
          build(stock, params)
        case StockMethod.Manual => build(item.stock.toString, params)
        case _ => ""
      }
    })
  }
}

@Tag("availability")
class TagAvailabilityTag extends ScalapressTag with TagBuilder {
  def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = {

    sreq.item.flatMap(item => {
      sreq.shoppingPlugin.stockMethod match {
        case StockMethod.Off => None
        case StockMethod.InOut => item.stock match {
          case 0 => Option(item.outStockMsg).orElse(Option(sreq.shoppingPlugin.outOfStockMessage))
          case _ => Some("In stock")
        }
        case _ =>
          item.stock match {
            case 0 =>
              val outMessage = Option(item.outStockMsg)
                .filterNot(_.isEmpty)
                .getOrElse(sreq.shoppingPlugin.outOfStockMessage)
              Some(build(outMessage, params))
            case _ => Some(build("In Stock", params))
          }
      }
    })
  }
}
