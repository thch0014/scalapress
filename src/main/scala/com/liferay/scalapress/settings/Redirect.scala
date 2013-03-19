package com.liferay.scalapress.settings

import javax.persistence.{Table, Entity, GenerationType, GeneratedValue, Id}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "redirects")
class Redirect {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var source: String = _
    @BeanProperty var target: String = _
}
