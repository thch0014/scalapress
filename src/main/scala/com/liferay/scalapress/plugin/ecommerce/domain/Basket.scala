package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{JoinColumn, ManyToOne, CascadeType, FetchType, OneToMany, Table, Entity, Id}
import reflect.BeanProperty
import java.util
import scala.collection.JavaConverters._
import com.liferay.scalapress.domain.ecommerce.Address

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
    @JoinColumn(name = "billing_address", nullable = true)
    @BeanProperty var billingAddress: Address = _

    //   @ManyToOne
    //   @JoinColumn(name = "account", nullable = true)
    //   @BeanProperty var account: Object = _

    def total: Int = {
        var total = 0
        for (line <- lines.asScala) {
            total = total + line.total
        }
        total
    }

    //    @BeanProperty var card: Card = _
    //
    //    @BeanProperty var deliveryOption: DeliveryOption = _
    //
    //    @BeanProperty var customerInstructions: String = _
    //
    //    @BeanProperty var lastAccessTime: Long = _
    //
    //    @BeanProperty var paymentType: PaymentType = _
    //
    //    @BeanProperty var customerReference: String = _
    //
    //    @BeanProperty var vatable: Boolean = _
    //
    //    @BeanProperty var account: Item = _
    //
    //    @BeanProperty var name: String = _
    //
    //    @BeanProperty var email: String = _
    //
    //    @BeanProperty var telephone1: String = _

}
