package com.cloudray.scalapress.obj

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.sksamuel.scoot.soa.Page
import com.cloudray.scalapress.Logging
import scala.collection.JavaConverters._
import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.search.Sort
import com.cloudray.scalapress.account.controller.Datum

/** @author Stephen Samuel */
trait ObjectDao extends GenericDao[Obj, java.lang.Long] {
  def recent(i: Int): Seq[Obj]
  def findBulk(longs: Seq[Long]): Seq[Obj]
  def search(query: ObjectQuery): Page[Obj]
  def typeAhead(query: String): Array[Datum]
  def findByType(id: Long): List[Obj]
  def removeByType(id: Long)
}

@Component
@Transactional
class ObjectDaoImpl extends GenericDaoImpl[Obj, java.lang.Long] with ObjectDao with Logging {

  override def recent(i: Int): Seq[Obj] =
    search(new Search(classOf[Obj])
      .addFilterEqual("status", Obj.STATUS_LIVE)
      .setMaxResults(i)
      .addSort("id", true))

  override def findBulk(longs: Seq[Long]): Seq[Obj] = {
    if (longs.isEmpty) Nil
    else {
      val s = new Search(classOf[Obj]).addFilterIn("id", longs.asJava)
      val results = search(s)
      results
    }
  }

  override def save(obj: Obj): Boolean = {
    obj.dateUpdated = System.currentTimeMillis()
    super.save(obj)
  }

  override def search(q: ObjectQuery): Page[Obj] = {
    val s = new Search(classOf[Obj]).setMaxResults(q.pageSize).setFirstResult(q.offset)
    q.typeId.foreach(t => {
      s.addFetch("objectType")
      s.addFilterEqual("objectType.id", t)
    })
    q.accountId.foreach(t => s.addFilterEqual("account.id", t))
    q.status.filterNot(_.isEmpty).foreach(s.addFilterEqual("status", _))
    q.name.filterNot(_.isEmpty).foreach(t => s.addFilterLike("name", "%" + t + "%"))
    q.minPrice.filter(_ > 0).foreach(s.addFilterGreaterOrEqual("price", _))
    s.setMaxResults(q.maxResults)
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

  override def findByType(id: Long): List[Obj] = search(new Search(classOf[Obj]).addFilterEqual("objectType.id", id))

  override def typeAhead(query: String): Array[Datum] = {
    getSession
      .createSQLQuery(
      "select a.id, a.name from items a WHERE a.name like ?")
      .setString(0, query + "%")
      .setMaxResults(20)
      .list()
      .asScala
      .map(arg => {
      val values = arg.asInstanceOf[Array[_]]
      Datum(value = values(1).toString, id = values(0).toString)
    }).toArray
  }

  override def removeByType(id: Long): Unit = getSession
    .createSQLQuery("delete from items where itemtype=" + id)
    .executeUpdate()
}

trait TypeDao extends GenericDao[ObjectType, java.lang.Long] {
  @deprecated
  def getAccount: Option[ObjectType]
}

@Component
@Transactional
class TypeDaoImpl extends GenericDaoImpl[ObjectType, java.lang.Long] with TypeDao {
  @deprecated
  def getAccount: Option[ObjectType] =
    findAll.find(t => t.name.toLowerCase == "account" || t.name.toLowerCase == "accounts")
}
