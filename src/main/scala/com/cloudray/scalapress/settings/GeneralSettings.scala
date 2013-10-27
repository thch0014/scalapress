package com.cloudray.scalapress.settings

import javax.persistence._
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.SingleInstance

/** @author Stephen Samuel */
@Entity
@SingleInstance
@Table(name = "settings_misc")
class GeneralSettings {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _
  @BeanProperty var toolbar: Boolean = false
  @BeanProperty var offlineMessage: String = null
  @BeanProperty var offline: Boolean = _

  @BeanProperty var maxImageWidth: Int = _
  @BeanProperty var maxImageHeight: Int = _

}
