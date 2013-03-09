package com.liferay.scalapress.domain

import scala.Predef.String
import javax.persistence.{Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "newsletters_subscribers")
class NewsletterSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty
    @BeanProperty var id: Long = _

    @BeanProperty var email: String = _

    @BeanProperty var date: Long = _

    @BeanProperty var validating: Boolean = _

    @BeanProperty var code: String = _

}
