package com.cloudray.scalapress.plugin.ecommerce.domain

import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.{Column, Entity, Table, GenerationType, GeneratedValue, Id}
import collection.mutable.ArrayBuffer
import scala.beans.BeanProperty

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

    @Column(name = "account", nullable = true)
    @BeanProperty var account: String = _

    @NotEmpty
    @Column(name = "addressLine1")
    @BeanProperty var address1: String = _

    @Column(name = "addressLine2")
    @BeanProperty var address2: String = _

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

    def label = {
        val sb = new ArrayBuffer[String]()
        Option(name).foreach(sb append _)
        Option(company).foreach(sb append _)
        Option(address1).foreach(sb append _)
        Option(address2).foreach(sb append _)
        Option(town).foreach(sb append _)
        Option(postcode).foreach(sb append _)
        Option(country).foreach(sb append _)
        Option(telephone).foreach(sb append _)
        sb.mkString("<br/>")
    }
}
