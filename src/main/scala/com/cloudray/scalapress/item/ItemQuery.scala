package com.cloudray.scalapress.item

import com.sksamuel.scoot.soa.PagedQuery
import scala.beans.BeanProperty
import com.cloudray.scalapress.search.Sort
import com.cloudray.scalapress.util.Page

/** @author Stephen Samuel */
class ItemQuery extends PagedQuery {

  @BeanProperty var typeId: Option[Long] = None
  @BeanProperty var accountId: Option[Long] = None
  @BeanProperty var status: Option[String] = None
  @BeanProperty var name: Option[String] = None
  @BeanProperty var exernalReference: Option[String] = None
  @BeanProperty var minPrice: Option[Int] = None
  @BeanProperty var maxPrice: Option[Int] = None
  @BeanProperty var sort: Option[Sort] = None

  def withSort(sort: Sort): ItemQuery = {
    this.sort = Option(sort)
    this
  }

  def withAccountId(a: Long): ItemQuery = {
    this.accountId = Option(a)
    this
  }

  def withName(name: String): ItemQuery = {
    this.name = Option(name)
    this
  }

  def withMinPrice(minPrice: Int): ItemQuery = {
    this.minPrice = Option(minPrice)
    this
  }

  def withMaxPrice(maxPrice: Int): ItemQuery = {
    this.maxPrice = Option(maxPrice)
    this
  }

  def withTypeId(typeId: Long): ItemQuery = {
    this.typeId = Option(typeId)
    this
  }

  def withStatus(status: String): ItemQuery = {
    this.status = Option(status)
    this
  }

  @deprecated
  def withPageSize(i: Int): ItemQuery = {
    this.pageSize = i
    this
  }

  def withPage(page: Page): ItemQuery = {
    this.pageSize = page.pageSize
    this
  }
}
