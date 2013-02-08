package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Entity, Table}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "messages")
class OrderComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne
    @JoinColumn(name = "order", nullable = true)
    @BeanProperty var order: Order = _

    @BeanProperty var date: Long = _

    @BeanProperty var author: String = _
    @BeanProperty var body: String = _
}
