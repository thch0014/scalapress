package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

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
        request.obj.map(obj => build(obj.stock.toString, params))
    }
}

object ObjectAvailabilityTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest,
               context: ScalapressContext,
               params: Map[String, String]): Option[String] = {
        request.obj.map(obj => build(obj.availability, params))
    }
}
