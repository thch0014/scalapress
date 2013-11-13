package com.cloudray.scalapress.item

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.sksamuel.scoot.soa.Page
import scala.collection.JavaConverters._
import com.googlecode.genericdao.search.Search
import com.cloudray.scalapress.search.Sort
import com.cloudray.scalapress.account.controller.Datum
import com.cloudray.scalapress.framework.Logging

/** @author Stephen Samuel */
trait ItemDao extends GenericDao[Item, java.lang.Long] {
  def recent(i: Int): Seq[Item]
  def findBulk(longs: Seq[Long]): Seq[Item]
  def search(query: ItemQuery): Page[Item]
  def typeAhead(query: String): Array[Datum]
  def findByType(id: Long): List[Item]
  def removeByType(id: Long)
}

@Component
@Transactional
class ItemDaoImpl extends GenericDaoImpl[Item, java.lang.Long] with ItemDao with Logging {

  override def recent(i: Int): Seq[Item] =
    search(new Search(classOf[Item])
      .addFilterEqual("status", Item.STATUS_LIVE)
      .setMaxResults(i)
      .addSort("id", true))

  override def findBulk(longs: Seq[Long]): Seq[Item] = {
    if (longs.isEmpty) Nil
    else {
      val s = new Search(classOf[Item]).addFilterIn("id", longs.asJava)
      val results = search(s)
      results
    }
  }

  override def save(item: Item): Boolean = {
    item.dateUpdated = System.currentTimeMillis
    super.save(item)
  }

  override def search(q: ItemQuery): Page[Item] = {
    val s = new Search(classOf[Item]).setMaxResults(q.pageSize).setFirstResult(q.offset)
    q.typeId.foreach(t => {
      s.addFetch("itemType")
      s.addFilterEqual("itemType.id", t)
    })
    q.accountId.foreach(t => s.addFilterEqual("account.id", t))
    q.status.filterNot(_.isEmpty).foreach(s.addFilterEqual("status", _))
    q.name.filterNot(_.isEmpty).foreach(t => s.addFilterLike("name", "%" + t + "%"))
    q.minPrice.filter(_ > 0).foreach(s.addFilterGreaterOrEqual("price", _))
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

  override def findByType(id: Long): List[Item] = search(new Search(classOf[Item]).addFilterEqual("itemType.id", id))

  override def typeAhead(query: String): Array[Datum] = {
    getSession
      .createSQLQuery("select a.id, a.name from items a WHERE a.name like ?")
      .setString(0, query + "%")
      .setMaxResults(20).list().asScala.map(arg => {
      val values = arg.asInstanceOf[Array[_]]
      Datum(value = values(1).toString, id = values(0).toString)
    }).toArray
  }

  override def removeByType(id: Long): Unit = getSession
    .createSQLQuery("delete from items where itemType=" + id).executeUpdate()
}

trait TypeDao extends GenericDao[ItemType, java.lang.Long] {
  @deprecated
  def getAccount: Option[ItemType]
}

@Component
@Transactional
class TypeDaoImpl extends GenericDaoImpl[ItemType, java.lang.Long] with TypeDao {
  @deprecated
  def getAccount: Option[ItemType] =
    findAll.find(t => t.name.toLowerCase == "account" || t.name.toLowerCase == "accounts")
}
