package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{CascadeType, OneToMany, ManyToOne, JoinColumn, Column, Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty
import com.liferay.scalapress.domain.Obj
import collection.mutable.ArrayBuffer

/** @author Stephen Samuel */
@Entity
@Table(name = "orders")
class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var status: String = _

    @BeanProperty var deliveryCharge: Int = _
    @BeanProperty var deliveryVatRate: Double = _

    def deliveryEx: Double = deliveryCharge / 100.0
    def deliveryVat: Double = if (vatable) deliveryEx * deliveryVatRate / 100.0 else 0
    def deliveryInc: Double = deliveryEx + deliveryVat

    @Column(name = "deliveryDetails")
    @BeanProperty var deliveryDetails: String = _

    @Column(name = "datePlaced")
    @BeanProperty var datePlaced: Long = _

    @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL))
    @BeanProperty var payments: java.util.List[Payment] = new java.util.ArrayList[Payment]()

    @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @BeanProperty var lines: java.util.List[OrderLine] = new java.util.ArrayList[OrderLine]()

    @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL), orphanRemoval = true)
    @BeanProperty var comments: java.util.List[OrderComment] = new java.util.ArrayList[OrderComment]()

    @ManyToOne
    @JoinColumn(name = "deliveryAddress")
    @BeanProperty var deliveryAddress: Address = _

    @ManyToOne
    @JoinColumn(name = "billingAddress")
    @BeanProperty var billingAddress: Address = _

    @BeanProperty var ipAddress: String = _

    @BeanProperty var vatable: Boolean = _

    @BeanProperty var customerReference: String = _

    @BeanProperty var reference: String = _

    @ManyToOne
    @JoinColumn(name = "account")
    @BeanProperty var account: Obj = _

    @Column(name = "salesPerson")
    @BeanProperty var createdBy: Long = _

    import scala.collection.JavaConverters._

    def linesSubtotal: Double = lines.asScala.map(_.totalExVat).foldLeft(0.0)((a, b) => a + b)
    def linesVat: Double = if (vatable) lines.asScala.map(_.totalVat).foldLeft(0.0)((a, b) => a + b) else 0.0
    def linesTotal: Double = lines.asScala.map(_.totalIncVat).foldLeft(0.0)((a, b) => a + b)

    def vat: Double = if (vatable) linesVat + deliveryVat else 0.0
    def subtotal: Double = linesSubtotal + deliveryEx
    def total: Double = ((linesTotal + deliveryInc) * 100).toInt / 100.0
}

object Order {
    def apply(ipAddress: String) = {
        val order = new Order
        order.datePlaced = System.currentTimeMillis()
        order.status = "New"
        order.ipAddress = ipAddress
        order.vatable = true
        order
    }
}
