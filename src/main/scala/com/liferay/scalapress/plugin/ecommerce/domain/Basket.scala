package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{Transient, JoinColumn, ManyToOne, CascadeType, FetchType, OneToMany, Table, Entity, Id}
import reflect.BeanProperty
import java.util
import scala.collection.JavaConverters._
import javax.validation.Valid
import org.hibernate.validator.constraints.{Email, NotEmpty}

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

    @NotEmpty
    @BeanProperty var accountName: String = _

    @BeanProperty var useBillingAddress = false

    @Email
    @NotEmpty
    @BeanProperty var accountEmail: String = _

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "basket", cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @BeanProperty var lines: java.util.List[BasketLine] = new util.ArrayList[BasketLine]()

    @Valid
    @ManyToOne(cascade = Array(CascadeType.ALL))
    @JoinColumn(name = "delivery_address", nullable = true)
    @BeanProperty var deliveryAddress: Address = _

    @Valid
    @ManyToOne(cascade = Array(CascadeType.ALL))
    @JoinColumn(name = "billing_address", nullable = true)
    @BeanProperty var billingAddress: Address = _

    @ManyToOne
    @JoinColumn(name = "delivery_option_id", nullable = true)
    @BeanProperty var deliveryOption: DeliveryOption = _

    def linesTotal: Double = {
        var total = 0.0
        for (line <- lines.asScala) {
            total = total + line.total
        }
        total
    }
    def total: Double = linesTotal + Option(deliveryOption).map(_.chargeIncVat).getOrElse(0)

    def linesSubtotal: Double = {
        var total = 0.0
        for (line <- lines.asScala) {
            total = total + line.subtotal
        }
        total
    }
    def subtotal: Double = linesSubtotal + Option(deliveryOption).map(_.charge).getOrElse(0)

    def linesVat: Double = {
        var total = 0.0
        for (line <- lines.asScala) {
            total = total + line.vat
        }
        total
    }
    def vat: Double = linesVat + Option(deliveryOption).map(_.chargeVat).getOrElse(0)
}
