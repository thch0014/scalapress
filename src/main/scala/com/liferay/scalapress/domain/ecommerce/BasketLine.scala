package com.liferay.scalapress.domain.ecommerce

import javax.persistence.{GenerationType, GeneratedValue, Id, JoinColumn, ManyToOne, Entity, Table}
import reflect.BeanProperty
import com.liferay.scalapress.domain.Obj

/** @author Stephen Samuel */
@Entity
@Table(name = "basketline")
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

    def total: Int = qty * obj.sellPrice
}

