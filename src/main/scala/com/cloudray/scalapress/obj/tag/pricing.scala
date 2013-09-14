package com.cloudray.scalapress.obj.tag

import com.cloudray.scalapress.{Tag, ScalapressRequest}
import com.cloudray.scalapress.enums.StockMethod
import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel */
object RrpTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.obj.flatMap(obj => {
      obj.rrp match {
        case 0 => None
        case _ =>
          val formatted = "&pound;%1.2f".format(obj.rrp / 100.0)
          Some(build(formatted, params))
      }
    })
  }
}

object RrpDiscountTag extends ScalapressTag with TagBuilder {

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.obj.flatMap(obj => {

      val saving = obj.rrp - obj.sellPriceInc
      saving match {
        case i if i <= 0 => None
        case _ =>
          val textFormatted = "&pound;%1.2f".format(saving / 100.0)
          Some(build(textFormatted, params))
      }
    })
  }
}

object ObjectSellPriceTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]) = {
    request.obj.map(obj => {

      val text = if (params.contains("ex")) obj.price
      else if (params.contains("vat") && request.installation.vatEnabled) obj.vat
      else if (params.contains("vat")) 0
      else if (request.installation.vatEnabled) obj.sellPriceInc
      else obj.price

      val textFormatted = "&pound;%1.2f".format(text / 100.0)
      build(textFormatted, params + ("class" -> "price"))

    })
  }
}

@Tag("stock")
class ObjectStockTag extends ScalapressTag with TagBuilder {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
    request.obj.map(obj => {
      request.shoppingPlugin.stockMethod match {
        case StockMethod.Automatic => build(obj.stock.toString, params)
        case StockMethod.InOut =>
          val stock = if (obj.stock > 0) "1" else "0"
          build(stock, params)
        case StockMethod.Manual => build(obj.stock.toString, params)
        case _ => ""
      }
    })
  }
}

@Tag("availability")
class ObjectAvailabilityTag extends ScalapressTag with TagBuilder {
  def render(sreq: ScalapressRequest, params: Map[String, String]): Option[String] = {

    sreq.obj.flatMap(obj => {
      sreq.shoppingPlugin.stockMethod match {
        case StockMethod.Off => None
        case StockMethod.InOut => obj.stock match {
          case 0 => Option(obj.outStockMsg).orElse(Option(sreq.shoppingPlugin.outOfStockMessage))
          case _ => Some("In stock")
        }
        case _ =>
          obj.stock match {
            case 0 => Some(build("In Stock", params))
            case _ =>
              val outMessage = Option(obj.outStockMsg)
                .filterNot(_.isEmpty)
                .getOrElse(sreq.shoppingPlugin.outOfStockMessage)
              Some(build(outMessage, params))
          }
      }
    })
  }
}
