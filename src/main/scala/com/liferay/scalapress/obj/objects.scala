package com.liferay.scalapress.obj

import org.springframework.stereotype.Component
import scala.collection.JavaConverters._
import com.googlecode.genericdao.search.Search
import com.liferay.scalapress.Logging
import org.springframework.transaction.annotation.Transactional
import com.sksamuel.scoot.soa.Page
import com.liferay.scalapress.util.{GenericDaoImpl, GenericDao}

/** @author Stephen Samuel */

trait ObjectDao extends GenericDao[Obj, java.lang.Long] {
    def search(query: ObjectQuery): Page[Obj]
    def typeAhead(query: String, objectTypeName: Option[String]): Array[Array[String]]
    def findByType(id: Long): List[Obj]
    def byEmail(email: String): Option[Obj]
}

@Component
@Transactional
class ObjectDaoImpl extends GenericDaoImpl[Obj, java.lang.Long] with ObjectDao with Logging {

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
        val result = searchAndCount(s)
        Page(result.getResult, q.pageNumber, q.pageSize, result.getTotalCount)
    }

    override def byEmail(email: String): Option[Obj] = {
        val search = new Search(classOf[Obj])
        search.addFilterEqual("email", email)
        Option(super.searchUnique(search))
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
