package com.cloudray.scalapress.settings

import javax.persistence._
import org.hibernate.annotations.CacheConcurrencyStrategy
import scala.beans.BeanProperty

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
