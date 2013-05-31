package com.cloudray.scalapress.obj

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.cloudray.scalapress.util.{GenericDaoImpl, GenericDao}
import com.sksamuel.scoot.soa.Page
import com.cloudray.scalapress.Logging
import scala.collection.JavaConverters._
import com.googlecode.genericdao.search.Search

/** @author Stephen Samuel */
trait ObjectDao extends GenericDao[Obj, java.lang.Long] {
    def recent(i: Int): Seq[Obj]
    def findBulk(longs: Seq[Long]): Seq[Obj]
    def search(query: ObjectQuery): Page[Obj]
    def typeAhead(query: String, objectTypeName: Option[String]): Array[Array[String]]
    def findByType(id: Long): List[Obj]
    def byEmail(email: String): Option[Obj]
}

@Component
@Transactional
class ObjectDaoImpl extends GenericDaoImpl[Obj, java.lang.Long] with ObjectDao with Logging {

    def recent(i: Int): Seq[Obj] = search(new Search(classOf[Obj]).setMaxResults(i).addSort("id", true))

    def findBulk(longs: Seq[Long]): Seq[Obj] = {
        val s = new Search(classOf[Obj]).addFilterIn("id", longs.asJava)
        search(s)
    }

    def search(q: ObjectQuery): Page[Obj] = {
        val s = new Search(classOf[Obj]).setMaxResults(q.pageSize).setFirstResult(q.offset).addSort("name", false)
        q.typeId.foreach(t => {
            s.addFetch("objectType")
            s.addFilterEqual("objectType.id", t)
        })
        q.accountId.foreach(t => {
            s.addFilterEqual("account.id", t)
        })
        q.status.filter(_.trim.length > 0).foreach(t => {
            s.addFilterEqual("status", t)
        })
        q.name.filter(_.trim.length > 0).foreach(t => {
            s.addFilterLike("name", "%" + t + "%")
        })
        s.addSortDesc("id")
        val result = searchAndCount(s)
        Page(result.getResult, q.pageNumber, q.pageSize, result.getTotalCount)
    }

    override def byEmail(email: String): Option[Obj] = {
        val search = new Search(classOf[Obj])
        search.addFilterEqual("email", email)
        super.search(search).headOption
    }

    def findByType(id: Long): List[Obj] = search(new Search(classOf[Obj]).addFilterEqual("objectType.id", id))

    override def typeAhead(query: String, objectTypeName: Option[String]): Array[Array[String]] = {
        getSession
          .createSQLQuery(
            "select i.id, i.name from items i join items_types it on i.itemtype=it.id where i.name like ? and it.name like ?")
          .setString(0, query + "%")
          .setString(1, "%" + objectTypeName.getOrElse("") + "%")
          .setMaxResults(20)
          .list()
          .asScala
          .map(arg => {
            val values = arg.asInstanceOf[Array[_]]
            Array(values(0).toString, values(1).toString)
        }).toArray
    }
}

trait TypeDao extends GenericDao[ObjectType, java.lang.Long] {
    def getAccount: Option[ObjectType]
}

@Component
@Transactional
class TypeDaoImpl extends GenericDaoImpl[ObjectType, java.lang.Long] with TypeDao {
    def getAccount: Option[ObjectType] =
        findAll.find(t => t.name.toLowerCase == "account" || t.name.toLowerCase == "accounts")
}
