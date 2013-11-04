package com.cloudray.scalapress.plugin.ecommerce.tags

import com.cloudray.scalapress.theme.tag.ScalapressTag
import com.cloudray.scalapress.plugin.ecommerce.dao.DeliveryOptionDao
import scala.xml.{Elem, Unparsed, Utility}
import com.cloudray.scalapress.plugin.ecommerce.domain.{Basket, DeliveryOption}
import com.cloudray.scalapress.framework.{ScalapressRequest, Tag}

/** @author Stephen Samuel */
@Tag("delivery_options")
class DeliveryOptionsTag extends ScalapressTag {
  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    val options = request.context.bean[DeliveryOptionDao].findAll.sortBy(_.position)
    val currentDeliveryId = Option(request.basket.deliveryOption).map(_.id.toString).orNull

    val radios = options.map(opt => {

      val selected = if (opt.id.toString == currentDeliveryId) "true" else null
      val price = Unparsed("&pound;%1.2f".format(opt.chargeIncVat / 100.0))

      val xml = <label class="radio">
        <input onclick="this.form.submit()"
               type="radio"
               name="deliveryOption"
               value={opt.id.toString}
               selected={selected}/>{opt.name}{price}
      </label>

      Utility.trim(xml)
    })

    Some(radios.mkString("\n"))
  }
}

@Tag("delivery_select")
class DeliverySelectTag extends ScalapressTag {

  def currentDeliveryId(basket: Basket): Long = Option(basket.deliveryOption).map(_.id).getOrElse(0)

  def delivery2option(delivery: DeliveryOption, currentDeliveryId: Long): Elem = {
    val selected = if (delivery.id == currentDeliveryId) "true" else null
    val price = Unparsed("&pound;%1.2f".format(delivery.chargeIncVat / 100.0))
    <option selected={selected} value={delivery.id.toString}>
      {delivery.name}&nbsp;{price}
    </option>
  }

  def render(request: ScalapressRequest, params: Map[String, String]): Option[String] = {

    val current = currentDeliveryId(request.basket)
    val deliveries = request.context.bean[DeliveryOptionDao].findAll.sortBy(_.position)
    val options = deliveries.map(delivery2option(_, current))

    val select =
      <select onclick="this.form.submit()" name="deliveryOption">
        {options}
      </select>

    Some(Utility.trim(select).toString())
  }
}