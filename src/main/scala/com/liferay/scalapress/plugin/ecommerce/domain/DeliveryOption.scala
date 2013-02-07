package com.liferay.scalapress.plugin.ecommerce.domain

import reflect.BeanProperty
import javax.persistence.{ElementCollection, Table, Entity, GenerationType, GeneratedValue, Id, Column}

/** @author Stephen Samuel */
@Entity
@Table(name = "delivery_options")
class DeliveryOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var name: String = _

    @BeanProperty var location: String = _

    @BeanProperty var saturdayDelivery: Boolean = _

    @Column(name = "flatCharge")
    @BeanProperty var charge: Int = _
    def chargeInc = charge + charge * vatRate

    // qualifying amount values
    @BeanProperty var startPrice: Int = _
    @BeanProperty var endPrice: Int = _

    @BeanProperty var vatRate: Double = _

    // valid for these areas only
    @BeanProperty var postcodes: String = _

    @ElementCollection
    @BeanProperty var countries: java.util.Set[String] = _
}
