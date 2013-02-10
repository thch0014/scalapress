package com.liferay.scalapress.service.theme.tag.obj

import com.liferay.scalapress.service.theme.tag.{TagBuilder, ScalapressTag}
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
object RrpTag extends ScalapressTag with TagBuilder {

    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        None
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
            val price = if (params.contains("inc")) obj.sellPriceInc else obj.getSellPrice
            price match {
                case 0 => ""
                case _ => build("%1.0f".format(price / 100.0), params + ("class" -> "price"))
            }
        })
    }
}

object ObjectStockTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        request.obj.map(_.stock.toString)
    }
}
