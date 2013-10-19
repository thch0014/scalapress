package com.cloudray.scalapress.obj

import com.sksamuel.scoot.soa.PagedQuery
import scala.beans.BeanProperty
import com.cloudray.scalapress.search.Sort

/** @author Stephen Samuel */
class ObjectQuery extends PagedQuery {

  @BeanProperty var typeId: Option[Long] = None
  @BeanProperty var accountId: Option[Long] = None
  @BeanProperty var status: Option[String] = None
  @BeanProperty var name: Option[String] = None
  @BeanProperty var exernalReference: Option[String] = None
  @BeanProperty var minPrice: Option[Int] = None
  @BeanProperty var maxPrice: Option[Int] = None
  @BeanProperty var sort: Option[Sort] = None
  @BeanProperty var maxResults: Int = 100

  def withSort(sort: Sort): ObjectQuery = {
    this.sort = Option(sort)
    this
  }

  def withMaxResults(i: Int) = {
    this.maxResults = i
    this
  }

  def withAccountId(a: Option[Long]): ObjectQuery = {
    this.accountId = a
    this
  }

  def withAccountId(a: Long): ObjectQuery = {
    this.accountId = Option(a)
    this
  }

  def withName(name: String): ObjectQuery = {
    this.name = Option(name)
    this
  }

  def withMinPrice(minPrice: Int): ObjectQuery = {
    this.minPrice = Option(minPrice)
    this
  }

  def withMaxPrice(maxPrice: Int): ObjectQuery = {
    this.maxPrice = Option(maxPrice)
    this
  }

  def withTypeId(typeId: Long): ObjectQuery = {
    this.typeId = Option(typeId)
    this
  }

  def withStatus(status: String): ObjectQuery = {
    this.status = Option(status)
    this
  }

  def withPageSize(i: Int): ObjectQuery = {
    this.pageSize = i
    this
  }
}
