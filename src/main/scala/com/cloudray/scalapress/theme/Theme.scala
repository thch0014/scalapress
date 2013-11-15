package com.cloudray.scalapress.theme

import javax.persistence._
import scala.beans.BeanProperty

/** @author Stephen Samuel */
@Entity
@Table(name = "templates")
class Theme {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty
  var id: Long = _

  @Column(name = "name", length = 256)
  @BeanProperty
  var name: String = _

  @Column(name = "header", length = 100000)
  @BeanProperty
  var header: String = _

  @Column(name = "footer", length = 100000)
  @BeanProperty
  var footer: String = _

  @Column(name = "dfault", nullable = false)
  @BeanProperty
  var default: Boolean = _

}
