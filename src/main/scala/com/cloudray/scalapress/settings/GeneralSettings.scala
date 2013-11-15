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
  @BeanProperty
  var id: Long = _

  @Column(name = "toolbar", nullable = false)
  @BeanProperty
  var toolbar: Boolean = false

  @Column(name = "offlineMessage", length = 10000)
  @BeanProperty
  var offlineMessage: String = null

  @Column(name = "offline", nullable = false)
  @BeanProperty
  var offline: Boolean = _

  @Column(name = "maxImageWidth")
  @BeanProperty
  var maxImageWidth: Int = _

  @Column(name = "maxImageHeight")
  @BeanProperty
  var maxImageHeight: Int = _

}
