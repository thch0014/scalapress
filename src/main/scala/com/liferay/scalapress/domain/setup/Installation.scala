package com.liferay.scalapress.domain.setup

import javax.persistence.{GenerationType, GeneratedValue, Id, Table, Entity}
import reflect.BeanProperty
import org.hibernate.annotations.CacheConcurrencyStrategy

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_site")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
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

    @BeanProperty var companyNumber: String = _

    @BeanProperty var country: String = null

    @BeanProperty var domain: String = "localhost"
}
