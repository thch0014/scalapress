package com.cloudray.scalapress.settings

import javax.persistence._
import scala.beans.BeanProperty

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

  @BeanProperty var maxImageWidth: Int = _
  @BeanProperty var maxImageHeight: Int = _

}
