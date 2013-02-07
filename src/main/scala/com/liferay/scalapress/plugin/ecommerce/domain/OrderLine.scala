package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{Column, Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "orders_lines")
class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty
    var id: Long = _

    @Column(name = "ord")
    var order: Long = _

    var salePrice: Int = _

    var date: Long = _

    var description: String = _

    var comment: String = _

    var optionsDescription: String = _

    @Column(name = "item")
    var obj: Long = _

    var promotion: Boolean = _

    var qty: Int = _
    var allocated: Int = _

    var vatRate: Double = _
    var position: Int = _
}
