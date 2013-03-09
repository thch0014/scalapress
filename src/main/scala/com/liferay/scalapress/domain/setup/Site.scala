package com.liferay.scalapress.domain.setup

import javax.persistence.{GenerationType, GeneratedValue, Id, Table, Entity}
import scala.Predef.String
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_site")
class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var postcode: String = null

    @BeanProperty var email: String = null

    @BeanProperty var telephone: String = null

    @BeanProperty var address: String = null

    @BeanProperty var name: String = null

    @BeanProperty var vatNumber: String = _
    @BeanProperty var companyNumber: String = _

    @BeanProperty var country: String = null
}
