package com.cloudray.scalapress.plugin.ecommerce.domain

import javax.persistence.{GenerationType, GeneratedValue, Id, JoinColumn, ManyToOne, Entity, Table}
import com.cloudray.scalapress.obj.Obj
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.variations.Variation
import org.hibernate.annotations.{NotFoundAction, NotFound}

/** @author Stephen Samuel */
@Entity
@Table(name = "plugin_shopping_baskets_lines")
class BasketLine {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  @ManyToOne
  @JoinColumn(name = "basket", nullable = true)
  @BeanProperty var basket: Basket = _

  @BeanProperty var qty: Int = _

  @ManyToOne
  @JoinColumn(name = "obj", nullable = true)
  @BeanProperty var obj: Obj = _

  @ManyToOne
  @JoinColumn(name = "variation", nullable = true)
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty var variation: Variation = _

  def description = obj.name + Option(variation).map(" " + _.name).getOrElse("")

  def price: Int = Option(variation).map(_.price).getOrElse(0) match {
    case 0 => obj.price
    case x: Int => x
  }
  def priceVat: Int = (price * obj.vatRate / 100.0).toInt
  def priceInc: Int = price + priceVat

  def subtotal: Int = price * qty
  def vat: Int = priceVat * obj.vat
  def total: Int = subtotal + vat
}

