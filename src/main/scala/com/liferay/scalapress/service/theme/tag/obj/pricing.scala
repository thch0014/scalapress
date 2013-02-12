package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.enums.StockMethod

/** @author Stephen Samuel */
object RrpTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.obj.flatMap(obj => {
            obj.rrp match {
                case 0 => None
                case _ =>
                    val textFormatted = "£%1.2f".format(obj.rrp / 100.0)
                    Some(build(textFormatted, params))
            }
        })
    }
}

object RrpDiscountTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        None
    }
}

object ObjectSellPriceTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]) = {
        request.obj.map(obj => {
            val text = if (params.contains("ex"))
                obj.getSellPrice
            else if (params.contains("vat"))
                obj.vat
            else
                obj.sellPriceInc
            val textFormatted = "£%1.2f".format(text / 100.0)

            build(textFormatted, params + ("class" -> "price"))
        })
    }
}

object ObjectStockTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.obj.map(obj => {
            context.shoppingPluginDao.get.stockMethod match {
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

object ObjectAvailabilityTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {

        request.obj.map(obj => {
            val plugin = context.shoppingPluginDao.get
            val msg = Option(obj.outStockMsg).filter(_.trim.length > 0).getOrElse(plugin.outOfStockMessage)
            plugin.stockMethod match {
                case StockMethod.Off => ""
                case _ =>
                    if (obj.stock > 0)
                        build("In Stock", params)
                    else
                        build(msg, params)
            }
        })
    }
}
