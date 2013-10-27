package com.cloudray.scalapress.plugin.listings.domain

import javax.persistence._
import scala.beans.BeanProperty
import com.cloudray.scalapress.plugin.SingleInstance

/** @author Stephen Samuel */
@Entity
@SingleInstance
@Table(name = "plugins_listings")
class ListingsPlugin {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id: Long = _

  def vatRate: Double = 0

  @BeanProperty var packagesPageText: String = _
  @BeanProperty var foldersPageText: String = _
  @BeanProperty var imagesPageText: String = _
}