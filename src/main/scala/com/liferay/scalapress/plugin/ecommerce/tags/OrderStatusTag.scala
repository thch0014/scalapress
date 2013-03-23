package com.liferay.scalapress.plugin.ecommerce.tags

import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}
import com.liferay.scalapress.theme.tag.{ScalapressTag, TagBuilder}

/** @author Stephen Samuel */
object OrderStatusTag extends ScalapressTag with TagBuilder {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val text = params.get("text").getOrElse("Order Status")
        val href = "/orderstatus"
        Some(buildLink(href, text, params))
    }
}
