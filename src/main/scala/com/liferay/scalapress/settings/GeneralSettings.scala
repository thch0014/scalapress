package com.liferay.scalapress.settings

import javax.persistence._
import reflect.BeanProperty
import org.hibernate.annotations.CacheConcurrencyStrategy

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_misc")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
class GeneralSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @BeanProperty var id: Long = _
    @BeanProperty var toolbar: Boolean = false
    @BeanProperty var offlineMessage: String = null
    @BeanProperty var offline: Boolean = _

}
