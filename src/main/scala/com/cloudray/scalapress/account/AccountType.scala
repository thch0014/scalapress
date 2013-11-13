package com.cloudray.scalapress.account

import javax.persistence._
import org.hibernate.annotations.Index
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "accounts_types")
class AccountType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Index(name = "id_index")
  @BeanProperty
  var id: Long = _

  @Index(name = "name_index")
  @BeanProperty
  var name: String = _

  @BeanProperty
  var registration: Boolean = false
}
