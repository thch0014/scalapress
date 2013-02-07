package com.liferay.scalapress.domain.setup

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

    @Column(name = "header", columnDefinition = "text")
    @BeanProperty var header: String = _

    @Column(name = "footer", columnDefinition = "text")
    @BeanProperty var footer: String = _

    @Column(name = "dfault")
    @BeanProperty var default: Boolean = _

}
