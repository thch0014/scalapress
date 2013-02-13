package com.liferay.scalapress.plugin.ecommerce.domain

import scala.Predef.String
import org.hibernate.validator.constraints.{Email, NotEmpty}
import reflect.BeanProperty
import javax.persistence.{Column, Entity, Table, GenerationType, GeneratedValue, Id}
import com.liferay.scalapress.domain.Obj

/** @author Stephen Samuel */
@Entity
@Table(name = "addresses")
class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @NotEmpty
    @BeanProperty var accountName: String = _

    @Email
    @NotEmpty
    @BeanProperty var accountEmail: String = _

    @NotEmpty
    @Column(name = "contactName")
    @BeanProperty var name: String = _

    @Column(name = "companyName")
    @BeanProperty var company: String = _

    @Column(name = "account", nullable = true)
    @BeanProperty var account: String = _

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

    @Column(length = 256)
    @BeanProperty var country: String = _

    @BeanProperty var date: Long = _

    @Column(name = "instructions", length = 1000)
    @BeanProperty var instructions: String = null

    @BeanProperty var state: String = null
    @BeanProperty var active: Boolean = _
}
