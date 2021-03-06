package com.cloudray.scalapress.plugin.ecommerce.shopping.domain

import javax.persistence.{JoinColumn, ManyToOne, Column, Table, Entity, GenerationType, GeneratedValue, Id}
import com.cloudray.scalapress.item.Item
import org.joda.time.{DateTime, DateTimeZone}
import scala.beans.BeanProperty
import org.hibernate.annotations.{NotFoundAction, NotFound}

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

  @NotFound(action = NotFoundAction.IGNORE)
  @Column(name = "item")
  @BeanProperty var item: Long = _

  @Column(name = "salePrice")
  @BeanProperty var price: Int = _

  @BeanProperty var date: Long = _

  @Column(name = "description")
  @BeanProperty var description: String = _

  @Column(name = "optionsDescription")
  @BeanProperty var options: String = _

  @BeanProperty var qty: Int = _

  @BeanProperty var vatRate: Double = _

  def priceExVat: Double = price / 100.0
  def priceVat: Double = if (order.vatable) priceExVat * vatRate / 100.0 else 0
  def priceIncVat: Double = priceExVat + priceVat

  def totalExVat: Double = qty * price / 100.0
  def totalVat: Double = if (order.vatable) totalExVat * vatRate / 100.0 else 0
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
  def apply(item: Item): OrderLine = {
    val l = new OrderLine
    l.price = item.price
    l.item = item.id
    l.qty = 1
    l.description = item.name
    l.date = new DateTime(DateTimeZone.UTC).getMillis
    l.vatRate = item.vatRate
    l
  }
}