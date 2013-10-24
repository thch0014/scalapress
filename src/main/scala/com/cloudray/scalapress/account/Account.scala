package com.cloudray.scalapress.account

import javax.persistence._
import scala.beans.BeanProperty
import org.joda.time.{DateTimeZone, DateTime}
import org.hibernate.annotations.{Index, NotFoundAction, NotFound}

/** @author Stephen Samuel */
@Entity
@Table(name = "accounts")
class Account {

  @Id
  @BeanProperty
  var id: Long = _

  @Index(name = "status_index")
  @BeanProperty
  var status: String = _

  def isActive = Account.STATUS_ACTIVE == status

  @Index(name = "name_index")
  @BeanProperty
  var name: String = _

  @Index(name = "email_index")
  @BeanProperty
  var email: String = _

  @BeanProperty
  var passwordHash: String = _

  @BeanProperty
  var registrationIpAddress: String = _

  @ManyToOne
  @JoinColumn(name = "accountType")
  @NotFound(action = NotFoundAction.IGNORE)
  @BeanProperty
  var accountType: AccountType = _

  @Column(name = "dateCreated")
  @BeanProperty
  var dateCreated: java.lang.Long = _

  @Column(name = "dateUpdated")
  @BeanProperty
  var dateUpdated: java.lang.Long = _

  @PrePersist
  def updatedDateUpdated(): Unit = dateUpdated = new DateTime(DateTimeZone.UTC).getMillis

  @PrePersist
  def updateDateCreated(): Unit = if (dateCreated == null) dateCreated = new DateTime(DateTimeZone.UTC).getMillis

  override def toString: String = s"Account [id=$id, status=$status, name=$name]"
}

object Account {
  val STATUS_ACTIVE = "Active"
  val STATUS_DISABLED = "Disabled"

  def apply(t: AccountType) = {
    require(t != null)

    val a = new Account
    a.id = System.currentTimeMillis() % 10000000
    a.accountType = t
    a.name = "new account"
    a.dateCreated = new DateTime(DateTimeZone.UTC).getMillis
    a.dateUpdated = a.dateCreated
    a.status = STATUS_ACTIVE
    a
  }
}