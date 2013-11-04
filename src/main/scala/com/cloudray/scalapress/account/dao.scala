package com.cloudray.scalapress.account

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.sksamuel.scoot.soa.Page
import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.search.Sort
import scala.collection.JavaConverters._
import com.cloudray.scalapress.account.controller.Datum
import com.cloudray.scalapress.framework.Logging

/** @author Stephen Samuel */
trait AccountDao extends GenericDao[Account, java.lang.Long] {
  def search(query: AccountQuery): Page[Account]
  def byEmail(email: String): Option[Account]
  def findByType(id: Long): List[Account]
  def typeAhead(query: String): Array[Datum]
}

@Component
@Transactional
class AccountDaoImpl extends GenericDaoImpl[Account, java.lang.Long] with AccountDao with Logging {

  override def typeAhead(query: String): Array[Datum] = {
    getSession
      .createSQLQuery(
      "select a.id, a.name from accounts a WHERE a.name like ?")
      .setString(0, query + "%")
      .setMaxResults(20)
      .list()
      .asScala
      .map(arg => {
      val values = arg.asInstanceOf[Array[_]]
      Datum(values(1).toString, values(0).toString)
    }).toArray
  }

  override def search(q: AccountQuery): Page[Account] = {
    val s = new Search(classOf[Account]).setMaxResults(q.pageSize).setFirstResult(q.offset)
    q.accountTypeId.foreach(t => {
      s.addFetch("accountType")
      s.addFilterEqual("accountType.id", t)
    })
    q.status.filterNot(_.isEmpty).foreach(s.addFilterEqual("status", _))
    q.name
      .map(_.split(" "))
      .getOrElse(Array())
      .foreach(t => s.addFilterLike("name", "%" + t + "%"))
    q.sort match {
      case Some(sort) => sort match {
        case Sort.Name => s.addSortAsc("name")
        case Sort.Newest => s.addSortDesc("id")
        case Sort.Oldest => s.addSortAsc("id")
        case Sort.Random => s.addSortDesc("id")
        case Sort.Price => s.addSortAsc("price")
        case Sort.PriceHigh => s.addSortDesc("price")
        case _ => s.addSortDesc("id")
      }
      case None => s.addSortDesc("id")
    }
    val result = searchAndCount(s)
    Page(result.getResult, q.pageNumber, q.pageSize, result.getTotalCount)
  }

  override def byEmail(email: String): Option[Account] = {
    val search = new Search(classOf[Account])
    search.addFilterEqual("email", email)
    super.search(search).headOption
  }

  override def findByType(id: Long): List[Account] =
    search(new Search(classOf[Account]).addFilterEqual("accountType.id", id))
}

trait AccountTypeDao extends GenericDao[AccountType, java.lang.Long] {
  def default: AccountType
}

@Component
@Transactional
class AccountTypeDaoImpl extends GenericDaoImpl[AccountType, java.lang.Long] with AccountTypeDao {
  def default = findAll.head
}

trait AccountPluginDao extends GenericDao[AccountPlugin, java.lang.Long] {
  def get: AccountPlugin
}

@Component
@Transactional
class AccountPluginDaoImpl extends GenericDaoImpl[AccountPlugin, java.lang.Long] with AccountPluginDao {
  def get = findAll.head
}

trait PasswordTokenDao extends GenericDao[PasswordToken, java.lang.Long] {
  def find(email: String, token: String): Option[PasswordToken]
}

@Component
@Transactional
class PasswordTokenDaoImpl extends GenericDaoImpl[PasswordToken, java.lang.Long] with PasswordTokenDao {
  def find(email: String, token: String): Option[PasswordToken] = {
    val search = new Search(classOf[PasswordToken])
    search.addFilterEqual("email", email)
    search.addFilterEqual("token", token)
    Option(super.searchUnique(search))
  }
}