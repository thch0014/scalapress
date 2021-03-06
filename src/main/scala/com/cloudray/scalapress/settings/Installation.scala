package com.cloudray.scalapress.settings

import javax.persistence._
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.SingleInstance

/** @author Stephen Samuel */
@Entity
@SingleInstance
@Table(name = "settings_site")
class Installation extends java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  // email for the software to send emails to
  @Column(name = "adminEmail", length = 1000)
  @BeanProperty
  var adminEmail: String = _

  @BeanProperty
  var postcode: String = _

  // public contact email
  @Column(name = "email", length = 1000)
  @BeanProperty
  var email: String = _

  @BeanProperty
  var telephone: String = _

  @Column(name = "address", length = 1000)
  @BeanProperty
  var address: String = _

  @Column(length = 1000)
  @BeanProperty
  var name: String = _

  @BeanProperty
  var vatNumber: String = _
  def vatEnabled: Boolean = Option(vatNumber).filter(_.trim.length > 0).isDefined

  @Column(name = "companyNumber")
  @BeanProperty
  var companyNumber: String = _

  @BeanProperty
  var country: String = _

  @BeanProperty
  var domain: String = "localhost"
}
