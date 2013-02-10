package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{JoinColumn, ManyToOne, Column, Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "orders_lines")
class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty
    @BeanProperty var id: Long = _

    @ManyToOne
    @JoinColumn(name = "ord")
    @BeanProperty var order: Order = _

    @Column(name = "item")
    @BeanProperty var obj: Long = _

    @Column(name = "salePrice")
    @BeanProperty var price: Int = _

    @BeanProperty var date: Long = _

    @Column(name = "description")
    @BeanProperty var description: String = _

    @Column(name = "optionsDescription")
    @BeanProperty var options: String = _

    @BeanProperty var qty: Int = _

    @BeanProperty var vatRate: Double = _

    def priceVat = price / 100.0 * vatRate / 100.0
    def priceExVat = price / 100.0
    def priceIncVat = priceExVat + priceVat
    def totalVat = qty * price / 100.0 * vatRate / 100.0
    def totalExVat = qty * price / 100.0
    def totalIncVat = totalExVat + totalVat
}

object OrderLine {
    def apply(line: BasketLine): OrderLine = {
        val l = new OrderLine
        l.price = line.obj.sellPrice
        l.obj = line.obj.id
        l.description = line.obj.name
        l.date = System.currentTimeMillis()
        l.vatRate = line.obj.vatRate
        l
    }
}