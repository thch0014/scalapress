package com.cloudray.scalapress.settings

import javax.persistence._
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "settings_site")
class Installation extends java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  // email for the software to send emails to
  @Column(length = 1000)
  @BeanProperty var adminEmail: String = _

  @BeanProperty var postcode: String = _

  // public contact email
  @Column(length = 1000)
  @BeanProperty var email: String = _

  @BeanProperty var telephone: String = _

  @Column(length = 1000)
  @BeanProperty var address: String = _

  @Column(length = 1000)
  @BeanProperty var name: String = _

  @BeanProperty var vatNumber: String = _
  def vatEnabled: Boolean = Option(vatNumber).filter(_.trim.length > 0).isDefined

  @BeanProperty var companyNumber: String = _

  @BeanProperty var country: String = _

  @BeanProperty var domain: String = "localhost"
}
