package com.cloudray.scalapress.plugin.ecommerce.shopping.dao

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.plugin.ecommerce.domain.Order
import com.googlecode.genericdao.search.Search
import com.sksamuel.scoot.soa.Page
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import scala.collection.JavaConverters._
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import com.cloudray.scalapress.plugin.ecommerce.shopping.dao.OrderQuery

/** @author Stephen Samuel */

trait OrderDao extends GenericDao[Order, java.lang.Long] {
  def search(q: OrderQuery): Page[Order]
  def emails: Seq[String]
  def ordersPerDay(limit: Int): Seq[OrderTotal]
}

case class OrderTotal(date: LocalDate, total: Int)

@Component
@Transactional
class OrderDaoImpl extends GenericDaoImpl[Order, java.lang.Long] with OrderDao {
  def search(q: OrderQuery): Page[Order] = {

    val s = new Search(classOf[Order]).setMaxResults(q.pageSize).setFirstResult(q.offset).addSort("id", true)
    q.orderId.filterNot(_.isEmpty).foreach(t => {
      s.addFilterEqual("id", t.toLong)
    })
    q.status.filterNot(_.isEmpty).foreach(t => {
      s.addFilterEqual("status", t)
    })
    q.name.filterNot(_.isEmpty).foreach(t => {
      s.addFetch("account")
      s.addFilterLike("account.name", "%" + t + "%")
    })
    q.from.foreach(timestamp => {
      s.addFilterGreaterOrEqual("datePlaced", timestamp)
    })
    q.to.foreach(timestamp => {
      s.addFilterLessOrEqual("datePlaced", timestamp)
    })
    q.accountId.foreach(accountId => {
      s.addFetch("account")
      s.addFilterEqual("account.id", accountId)
    })
    val result = searchAndCount(s)
    Page(result.getResult, q.pageNumber, q.pageSize, result.getTotalCount)
  }

  def emails: Seq[String] = getSession
    .createSQLQuery("select distinct email from items")
    .list()
    .asScala
    .toSeq
    .asInstanceOf[Seq[String]]

  def ordersPerDay(limit: Int): Seq[OrderTotal] = {
    val query = super.getSession.createSQLQuery(
      "SELECT count(*), from_unixtime(datePlaced / 1000, \"%Y-%m-%d\") as date FROM orders GROUP BY date ORDER BY datePlaced DESC limit 365")
    val results = query.list().asInstanceOf[java.util.List[Array[Object]]].asScala.reverse
    val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
    for ( result <- results ) yield OrderTotal(formatter.parseLocalDate(result(1).toString), result(0).toString.toInt)
  }
}
