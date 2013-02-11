package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{JoinColumn, ManyToOne, CascadeType, FetchType, OneToMany, Table, Entity, Id}
import reflect.BeanProperty
import java.util
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_shopping_baskets")
class Basket {

    def empty() {
        lines.clear()
        deliveryOption = null
    }

    @Id
    @BeanProperty var sessionId: String = _

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "basket", cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @BeanProperty var lines: java.util.List[BasketLine] = new util.ArrayList[BasketLine]()

    @ManyToOne
    @JoinColumn(name = "delivery_address", nullable = true)
    @BeanProperty var deliveryAddress: Address = _

    @ManyToOne
    @JoinColumn(name = "delivery_option_id", nullable = true)
    @BeanProperty var deliveryOption: DeliveryOption = _

    def linesTotal: Int = {
        var total = 0
        for (line <- lines.asScala) {
            total = total + line.total
        }
        total
    }
    def total: Int = linesTotal + Option(deliveryOption).map(_.chargeIncVat).getOrElse(0)

    def linesSubtotal: Int = {
        var total = 0
        for (line <- lines.asScala) {
            total = total + line.total
        }
        total
    }
    def subtotal: Int = linesSubtotal + Option(deliveryOption).map(_.charge).getOrElse(0)

    def linesVat: Int = {
        var total = 0
        for (line <- lines.asScala) {
            total = total + line.total
        }
        total
    }
    def vat: Int = linesVat + Option(deliveryOption).map(_.chargeVat).getOrElse(0)
}
