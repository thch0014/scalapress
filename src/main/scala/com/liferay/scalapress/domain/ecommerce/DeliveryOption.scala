package com.liferay.scalapress.domain.ecommerce

import reflect.BeanProperty

/** @author Stephen Samuel */
class DeliveryOption {

    @BeanProperty var countries: java.util.Set[String] = _

    @BeanProperty var cutOff: Int = _

    @BeanProperty var qtyIncrement: Int = _

    @BeanProperty var startQty: Int = _

    @BeanProperty var endQty: Int = _

    @BeanProperty var period: Int = _
    /**
     * Should be just interval or valueInterval or something. BC
     */

    @BeanProperty var weightIncrement: Double = _

    /**
     * Start weight / end weight
     * Should be start / end attribute value but keeping for BC
     */
    @BeanProperty var startWeight: Double = _

    @BeanProperty var endWeight: Double = _

    @BeanProperty var name: String = _

    @BeanProperty var location: String = _

    @BeanProperty var saturdayDelivery: Boolean = _

    @BeanProperty var weightIncrementCharge: Int = _

    @BeanProperty var startPrice: Int = _

    @BeanProperty var endPrice: Int = _

    @BeanProperty var flatCharge: Int = _

    @BeanProperty var qtyIncrementCharge: Int = _

    @BeanProperty var priceIncrementCharge: Int = _

    @BeanProperty var priceIncrement: Int = _
    //   @BeanProperty var `type`: DeliveryOption.Type = _
    @BeanProperty var vatRate: Double = _

    @BeanProperty var position: Int = _

    @BeanProperty var code: String = _
    @BeanProperty var saturdayDispatch: Boolean = _
    @BeanProperty var saturdayService: Boolean = _
    @BeanProperty var postcodes: String = _
}
