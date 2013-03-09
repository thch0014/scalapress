package com.liferay.scalapress.domain.ecommerce

import javax.persistence.{GenerationType, GeneratedValue, Id, Entity, Table}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Table(name = "vouchers")
@Entity
class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty
    var id: Long = _

    @BeanProperty var name: String = null

    @BeanProperty var qualifyingAmount: Int = _
    @BeanProperty var qualifyingQty: Int = _

    @BeanProperty var code: String = _

    @BeanProperty var startDate: Long = _
    @BeanProperty var endDate: Long = _

    // voucher provides a discount on the order
    @BeanProperty var discount: Int = _

    // % discount as an int, eg 2 is 2%
    @BeanProperty var percentage: Int = _

    // voucher provides free shipping
    @BeanProperty var freeDelivery: Boolean = _

    @BeanProperty var maxUsesPerMember: Int = _
    @BeanProperty var firstOrderOnly: Boolean = _
}
