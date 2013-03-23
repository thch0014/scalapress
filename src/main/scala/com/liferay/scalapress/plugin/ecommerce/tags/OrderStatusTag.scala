package com.liferay.scalapress.plugin.ecommerce.tags

import com.liferay.scalapress.{Tag, ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel */
@Tag("order_status")
class OrderStatusTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val text = params.get("text").getOrElse("Order Status")
        val href = "/orderstatus"
        Some(buildLink(href, text, params))
    }
}
