package com.cloudray.scalapress.plugin.ecommerce.domain

import javax.persistence.{ElementCollection, Table, Entity, GenerationType, GeneratedValue, Id, Column}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "delivery")
class DeliveryOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var deleted: Int = 0

    @BeanProperty var name: String = _

    @Column(name = "flatCharge")
    @BeanProperty var charge: Int = _
    @BeanProperty def chargeVat: Int = (charge * vatRate / 100.0).toInt
    @BeanProperty def chargeIncVat: Int = charge + chargeVat
    @BeanProperty var vatRate: Double = _

    // qualifying amount values
    @Column(name = "startPrice")
    @BeanProperty var minPrice: Int = _

    @Column(name = "endPrice")
    @BeanProperty var maxPrice: Int = _

    // valid for these areas only
    @BeanProperty var postcodes: String = _

    @BeanProperty var position: Int = _

    @ElementCollection
    @BeanProperty var countries: java.util.Set[String] = _

    @BeanProperty var saturdayDelivery: Boolean = _
}
