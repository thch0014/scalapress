package com.cloudray.scalapress.account

import javax.persistence._
import scala.beans.BeanProperty
import org.joda.time.{DateTimeZone, DateTime}
import org.hibernate.annotations.{NotFoundAction, NotFound, Index}

/** @author Stephen Samuel */
@Entity
@Table(name = "accounts")
class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Index(name = "id_index")
  @BeanProperty var id: Long = _

  @Index(name = "status_index")
  @BeanProperty var status: String = _

  @Index(name = "name_index")
  @BeanProperty var name: String = _

  @Index(name = "email_index")
  @BeanProperty var email: String = _

  @BeanProperty var passwordHash: String = _

  @BeanProperty var registrationIpAddress: String = _

  @ManyToOne
  @JoinColumn(name = "accountType")
  @NotFound(action = NotFoundAction.IGNORE)
  @Index(name = "accounttype_index")
  @BeanProperty var accountType: AccountType = _

  @Column(name = "dateCreated")
  @BeanProperty var dateCreated: java.lang.Long = _

  @Column(name = "dateUpdated")
  @BeanProperty var dateUpdated: java.lang.Long = _

  @PrePersist
  def updateLastModified(): Unit = dateUpdated = new DateTime(DateTimeZone.UTC).getMillis
}

object Account {
  val STATUS_ACTIVE = "active"
}