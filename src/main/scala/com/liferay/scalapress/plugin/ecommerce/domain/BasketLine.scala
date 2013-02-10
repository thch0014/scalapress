package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{GenerationType, GeneratedValue, Id, JoinColumn, ManyToOne, Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.domain.Obj

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

    // total value of the line excluding VAT
    def subtotal: Int = qty * obj.sellPrice
    def vat: Int = qty * obj.vat
    // total value of the line including VAT
    def total: Int = subtotal + vat
}

