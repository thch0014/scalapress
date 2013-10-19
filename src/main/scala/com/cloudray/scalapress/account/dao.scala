package com.cloudray.scalapress.account

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.sksamuel.scoot.soa.Page
import com.cloudray.scalapress.Logging
import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.search.Sort

/** @author Stephen Samuel */
trait AccountDao extends GenericDao[Account, java.lang.Long] {
  def search(query: AccountQuery): Page[Account]
  def byEmail(email: String): Option[Account]
  def findByType(id: Long): List[Account]
}

@Component
@Transactional
class AccountDaoImpl extends GenericDaoImpl[Account, java.lang.Long] with AccountDao with Logging {

  override def search(q: AccountQuery): Page[Account] = {
    val s = new Search(classOf[Account]).setMaxResults(q.pageSize).setFirstResult(q.offset)
    q.typeId.foreach(t => {
      s.addFetch("accountType")
      s.addFilterEqual("accountType.id", t)
    })
    q.status.filterNot(_.isEmpty).foreach(s.addFilterEqual("status", _))
    q.name.filterNot(_.isEmpty).foreach(t => s.addFilterLike("name", "%" + t + "%"))
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

trait AccountTypeDao extends GenericDao[AccountType, java.lang.Long]

@Component
@Transactional
class AccountTypeDaoImpl extends GenericDaoImpl[AccountType, java.lang.Long] with AccountTypeDao