package com.liferay.scalapress.theme

import scala.Predef.String
import javax.persistence.{Table, Entity, GenerationType, GeneratedValue, Id}
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "markup")
class Markup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var name: String = _
    @BeanProperty var body: String = _
    @BeanProperty var start: String = _
    @BeanProperty var end: String = _
    @BeanProperty var css: String = _
    @BeanProperty var between: String = _

}
