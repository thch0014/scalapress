package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{CascadeType, OneToMany, ManyToOne, JoinColumn, Column, Table, Entity, GenerationType, GeneratedValue, Id}
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

    @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL))
    @BeanProperty var payments: java.util.List[Payment] = new java.util.ArrayList[Payment]()

    @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL))
    @BeanProperty var lines: java.util.List[OrderLine] = new java.util.ArrayList[OrderLine]()

    @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL))
    @BeanProperty var comments: java.util.List[OrderComment] = new java.util.ArrayList[OrderComment]()

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
    @BeanProperty var account: Obj = _

    @Column(name = "salesPerson")
    @BeanProperty var createdBy: Long = _

    import scala.collection.JavaConverters._

    def totalVat = lines.asScala.map(_.totalVat).foldLeft(0.0)((a, b) => a + b)
    def totalExVat = lines.asScala.map(_.totalExVat).foldLeft(0.0)((a, b) => a + b)
    def totalIncVat = lines.asScala.map(_.totalIncVat).foldLeft(0.0)((a, b) => a + b)
}

object Order {
    def apply(ipAddress: String) = {
        val order = new Order
        order.datePlaced = System.currentTimeMillis()
        order.status = "New"
        order.ipAddress = ipAddress
        order
    }
}
