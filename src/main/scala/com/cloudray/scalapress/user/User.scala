package com.cloudray.scalapress.user

import javax.persistence.{Transient, Column, GenerationType, GeneratedValue, Id, Entity, Table}
import scala.Predef.String
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Table(name = "users")
@Entity
class User extends java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: java.lang.Long = _

  @Column(name = "active", nullable = false)
  @BeanProperty var active: Boolean = _

  @Column(name = "name")
  @BeanProperty var name: String = _

  @Column(name = "username")
  @BeanProperty var username: String = _

  @Column(name = "passwordHash")
  @BeanProperty var passwordHash: String = _

  @Transient
  @BeanProperty var changePassword: String = _
}
