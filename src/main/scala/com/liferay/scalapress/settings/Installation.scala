package com.liferay.scalapress.settings

import javax.persistence.{GenerationType, GeneratedValue, Id, Table, Entity}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_site")
class Installation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var postcode: String = null

    @BeanProperty var email: String = null

    @BeanProperty var telephone: String = null

    @BeanProperty var address: String = null

    @BeanProperty var name: String = null

    @BeanProperty var vatNumber: String = _
    def vatEnabled: Boolean = Option(vatNumber).filter(_.trim.length > 0).isDefined

    @BeanProperty var companyNumber: String = _

    @BeanProperty var country: String = null

    @BeanProperty var domain: String = "localhost"
}