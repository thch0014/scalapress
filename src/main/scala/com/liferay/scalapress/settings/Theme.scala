package com.liferay.scalapress.settings

import javax.persistence.{GenerationType, GeneratedValue, Id, Column, Table, Entity}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "templates")
class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _

    @BeanProperty var name: String = _

    @Column(name = "header", length = 10000, nullable = true)
    @BeanProperty var header: String = _

    @Column(name = "footer", length = 10000, nullable = true)
    @BeanProperty var footer: String = _

    @Column(name = "dfault")
    @BeanProperty var default: Boolean = _

}