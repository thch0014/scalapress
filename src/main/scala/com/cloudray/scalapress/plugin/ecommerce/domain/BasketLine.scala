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

  // total value of the line excluding VAT
  def subtotal: Double = {
    val price = Option(variation) match {
      case None => obj.price
      case Some(v) => v.price
    }
    price * qty
  }

  def vat: Double = qty * obj.vat

  // total value of the line including VAT
  def total: Double = subtotal + vat
}

