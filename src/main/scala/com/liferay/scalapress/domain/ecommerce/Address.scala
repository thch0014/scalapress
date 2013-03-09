package com.liferay.scalapress.domain.ecommerce

import scala.Predef.String
import org.hibernate.validator.constraints.NotEmpty
import reflect.BeanProperty
import javax.persistence.{Entity, Table, GenerationType, GeneratedValue, Id, JoinColumn}

/** @author Stephen Samuel */
@Entity
@Table(name = "addresses")
class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @NotEmpty
    @BeanProperty var telephone: String = _

    @BeanProperty var active: Boolean = _

    @NotEmpty
    @BeanProperty var addressLine1: String = _
    @BeanProperty var addressLine2: String = _

    @BeanProperty var addressLine3: String = _

    @BeanProperty var companyName: String = _

    @BeanProperty var county: String = _

    @NotEmpty
    @BeanProperty var contactName: String = _

    @NotEmpty
    @BeanProperty var town: String = _

    @BeanProperty var postcode: String = _

    @BeanProperty var country: String = _

    @JoinColumn(name = "account")
    @BeanProperty var owner: Long = _

    @BeanProperty var date: Long = _

    @BeanProperty var instructions: String = null

    @BeanProperty var state: String = null
}
