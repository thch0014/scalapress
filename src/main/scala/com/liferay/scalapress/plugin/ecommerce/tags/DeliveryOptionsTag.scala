package com.liferay.scalapress.plugin.ecommerce.tags

import com.liferay.scalapress.{Tag, ScalapressRequest}
import com.liferay.scalapress.theme.tag.ScalapressTag

/** @author Stephen Samuel */
@Tag("delivery_options")
class DeliveryOptionsTag extends ScalapressTag {
    def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

        val options = request.context.deliveryOptionDao.findAll().sortBy(_.position)
        val currentDeliveryId = Option(request.basket.deliveryOption).map(_.id.toString).orNull

        val radios = options.map(opt => {
            val selected = if (opt.id.toString == currentDeliveryId) "checked='true'" else ""
            val price = "&pound;%1.2f".format(opt.chargeIncVat / 100.0)
            "<label class=\"radio\">" +
              "<input onclick='this.form.submit()' type=\"radio\" name=\"deliveryOption\" value=\"" + opt
              .id + "\" " + selected + ">" +
              "</input>" + opt.name + " " + price + "</label>"
        })

        Some(radios.mkString("\n"))
    }
}