package com.liferay.scalapress.plugin.ecommerce.domain

import javax.persistence.{JoinColumn, ManyToOne, GenerationType, GeneratedValue, Id, Entity, Table}
import scala.beans.BeanProperty
import org.joda.time.{DateTimeZone, DateTime}

/** @author Stephen Samuel */
@Entity
@Table(name = "messages")
class OrderComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @ManyToOne
    @JoinColumn(name = "`order`", nullable = true)
    @BeanProperty var order: Order = _

    @BeanProperty var date: Long = _

    @BeanProperty var author: String = _
    @BeanProperty var body: String = _
}

object OrderComment {
    def apply(order: Order, body: String): OrderComment = {
        val c = new OrderComment
        c.date = new DateTime(DateTimeZone.UTC).getMillis
        c.order = order
        c.body = body
        c.author = "System"
        c
    }
}