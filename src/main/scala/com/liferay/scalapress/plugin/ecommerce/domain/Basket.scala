package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{JoinColumn, ManyToOne, CascadeType, FetchType, OneToMany, Table, Entity, Id}
import reflect.BeanProperty
import java.util
import scala.collection.JavaConverters._

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_shopping_basket")
class Basket {

    @Id
    @BeanProperty var id: String = _

    @BeanProperty var sessionId: String = _

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "basket", cascade = Array(CascadeType.ALL))
    @BeanProperty var lines: java.util.List[BasketLine] = new util.ArrayList[BasketLine]()

    @ManyToOne
    @JoinColumn(name = "delivery_address", nullable = true)
    @BeanProperty var deliveryAddress: Address = _

    @ManyToOne
    @JoinColumn(name = "delivery_address", nullable = true)
    @BeanProperty var deliveryOption: DeliveryOption = _

    def total: Int = {
        var total = 0
        for (line <- lines.asScala) {
            total = total + line.total
        }
        total
    }

}
