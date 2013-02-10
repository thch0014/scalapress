package com.liferay.scalapress.domain.setup

import javax.persistence.{GenerationType, GeneratedValue, Id, Table, Entity}
import reflect.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_misc")
class GeneralSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _
    @BeanProperty var toolbar: Boolean = false
    @BeanProperty var offlineMessage: String = null
    @BeanProperty var offline: Boolean = _

}
