package com.liferay.scalapress.plugin.ecommerce.tags

import com.liferay.scalapress.service.theme.tag.ScalapressTag
import com.liferay.scalapress.{ScalapressContext, ScalapressRequest}

/** @author Stephen Samuel */
object DeliveryOptionsTag extends ScalapressTag {
    def render(request: ScalapressRequest, context: ScalapressContext, params: Map[String, String]): Option[String] = {
        val options = context.deliveryOptionDao.findAll()
        val currentDeliveryId = Option(request.basket.deliveryOption).map(_.id.toString).orNull
        val radios = options.map(opt => {
            val selected = if (opt.id.toString == currentDeliveryId) "checked='true'" else ""
            "<label class=\"radio\">" +
              "<input type=\"radio\" name=\"deliveryOption\" value=\"" + opt.id + "\" " + selected + ">" +
              "</input>" + opt.name + " &pound;" + opt.chargeIncVat + "</label>"
        })
        Some(radios.mkString("\n"))
    }
}