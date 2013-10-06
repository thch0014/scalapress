package com.cloudray.scalapress.obj

import com.sksamuel.scoot.soa.PagedQuery
import scala.beans.BeanProperty

/** @author Stephen Samuel */
class ObjectQuery extends PagedQuery {

  @BeanProperty var typeId: Option[Long] = None
  @BeanProperty var accountId: Option[Long] = None
  @BeanProperty var status: Option[String] = None
  @BeanProperty var name: Option[String] = None
  @BeanProperty var exernalReference: Option[String] = None
  @BeanProperty var minPrice: Option[Int] = None

  def withAccountId(a: Option[Long]) = {
    this.accountId = a
    this
  }

  def withName(name: String) = {
    this.name = Option(name)
    this
  }

  def withTypeId(typeId: Long) = {
    this.typeId = Option(typeId)
    this
  }

  def withStatus(status: String) = {
    this.status = Option(status)
    this
  }

  def withPageSize(i: Int) = {
    this.pageSize = i
    this
  }
}
