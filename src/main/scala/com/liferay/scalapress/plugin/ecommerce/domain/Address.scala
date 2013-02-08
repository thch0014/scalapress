package com.liferay.scalapress.plugin.ecommerce.domain

import scala.Predef.String
import org.hibernate.validator.constraints.NotEmpty
import reflect.BeanProperty
import javax.persistence.{Column, Entity, Table, GenerationType, GeneratedValue, Id}

/** @author Stephen Samuel */
@Entity
@Table(name = "addresses")
class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @NotEmpty
    @Column(name = "contactName")
    @BeanProperty var name: String = _

    @Column(name = "companyName")
    @BeanProperty var company: String = _

    @Column(name = "account")
    @BeanProperty var owner: Long = _

    @NotEmpty
    @Column(name = "addressLine1")
    @BeanProperty var address1: String = _

    @Column(name = "addressLine2")
    @BeanProperty var address2: String = _

    @Column(name = "addressLine3")
    @BeanProperty var address3: String = _

    @NotEmpty
    @BeanProperty var town: String = _

    @NotEmpty
    @BeanProperty var postcode: String = _

    @NotEmpty
    @BeanProperty var telephone: String = _

    @BeanProperty var country: String = _

    @BeanProperty var date: Long = _

    @BeanProperty var instructions: String = null

    @BeanProperty var state: String = null
    @BeanProperty var active: Boolean = _
}