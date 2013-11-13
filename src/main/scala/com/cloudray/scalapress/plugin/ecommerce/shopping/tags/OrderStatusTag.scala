package com.cloudray.scalapress.plugin.ecommerce.tags

import com.cloudray.scalapress.theme.tag.{ScalapressTag, TagBuilder}
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("order_status")
class OrderStatusTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {
        val text = params.get("text").getOrElse("Order Status")
        val href = "/orderstatus"
        Some(buildLink(href, text, params))
    }
}
