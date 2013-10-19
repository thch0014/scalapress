package com.cloudray.scalapress.account

import com.sksamuel.scoot.soa.PagedQuery
import scala.beans.BeanProperty
import com.cloudray.scalapress.search.Sort

/** @author Stephen Samuel */
class AccountQuery extends PagedQuery {

  @BeanProperty var typeId: Option[Long] = None
  @BeanProperty var status: Option[String] = None
  @BeanProperty var name: Option[String] = None
  @BeanProperty var sort: Option[Sort] = None

  def withSort(sort: Sort): AccountQuery = {
    this.sort = Option(sort)
    this
  }

  def withName(name: String): AccountQuery = {
    this.name = Option(name)
    this
  }

  def withTypeId(typeId: Long): AccountQuery = {
    this.typeId = Option(typeId)
    this
  }

  def withStatus(status: String): AccountQuery = {
    this.status = Option(status)
    this
  }

  def withPageSize(i: Int): AccountQuery = {
    this.pageSize = i
    this
  }
}
