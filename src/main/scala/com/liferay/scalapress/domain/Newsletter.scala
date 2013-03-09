package com.liferay.scalapress.domain

import scala.Predef.String
import reflect.BeanProperty
import javax.persistence.{Entity, Table, GenerationType, GeneratedValue, Id}

/** @author Stephen Samuel */
@Entity
@Table(name = "newsletters")
class Newsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var name: String = null
    @BeanProperty var description: String = null
}
