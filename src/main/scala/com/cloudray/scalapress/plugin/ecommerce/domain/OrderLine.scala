package com.cloudray.scalapress.plugin.ecommerce.domain

import javax.persistence.{JoinColumn, ManyToOne, Column, Table, Entity, GenerationType, GeneratedValue, Id}
import com.cloudray.scalapress.obj.Obj
import org.joda.time.{DateTime, DateTimeZone}
import scala.beans.BeanProperty

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

    def priceVat: Double = if (order.vatable) price / 100.0 * vatRate / 100.0 else 0
    def priceExVat: Double = price / 100.0
    def priceIncVat: Double = priceExVat + priceVat

    def totalVat: Double = if (order.vatable) qty * price / 100.0 * vatRate / 100.0 else 0
    def totalExVat: Double = qty * price / 100.0
    def totalIncVat: Double = totalExVat + totalVat
}

object OrderLine {
    def apply(line: BasketLine): OrderLine = {
        val l = apply(line.obj)
        l.qty = line.qty
        l
    }
    def apply(desc: String, price: Int): OrderLine = {
        val l = new OrderLine
        l.price = price
        l.vatRate = 20.0
        l.date = new DateTime(DateTimeZone.UTC).getMillis
        l.qty = 1
        l.description = desc
        l
    }
    def apply(obj: Obj): OrderLine = {
        val l = new OrderLine
        l.price = obj.sellPrice
        l.obj = obj.id
        l.qty = 1
        l.description = obj.name
        l.date = new DateTime(DateTimeZone.UTC).getMillis
        l.vatRate = obj.vatRate
        l
    }
}