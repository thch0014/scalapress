package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{OneToMany, ManyToOne, JoinColumn, Column, Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.domain.Obj

/** @author Stephen Samuel */
@Entity
@Table(name = "orders")
class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var status: String = _

    @BeanProperty var saleVat: Int = _
    @BeanProperty var saleSubtotal: Int = _
    @BeanProperty var saleTotal: Int = _

    @BeanProperty var deliveryCharge: Int = _
    @BeanProperty var deliveryChargeVat: Int = _
    @BeanProperty var deliveryChargeInc: Int = _
    @BeanProperty var deliveryVatRate: Double = _

    @Column(name = "datePlaced")
    @BeanProperty var datePlaced: Long = _

    @OneToMany(mappedBy = "order")
    @BeanProperty var payments: java.util.List[Payment] = new java.util.ArrayList[Payment]()

    @ManyToOne
    @JoinColumn(name = "deliveryAddress")
    @BeanProperty var deliveryAddress: Address = _

    @BeanProperty var ipAddress: String = _

    @BeanProperty var customerReference: String = _
    @BeanProperty var reference: String = _

    @Column(name = "deliveryDetails")
    @BeanProperty var deliveryDetails: String = _

    @ManyToOne
    @JoinColumn(name = "account")
    var account: Obj = _

    @Column(name = "salesPerson")
    var createdBy: Long = _
}
