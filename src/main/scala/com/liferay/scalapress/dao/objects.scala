package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import org.hibernate.criterion.Restrictions
import scala.collection.JavaConverters._
import com.liferay.scalapress.domain.Obj
import org.hibernate.FetchMode
import com.googlecode.genericdao.search.Search
import com.liferay.scalapress.{Page, Logging}
import org.springframework.transaction.annotation.Transactional
import com.liferay.scalapress.controller.admin.obj.ObjectQuery

/** @author Stephen Samuel */

trait ObjectDao extends GenericDao[Obj, java.lang.Long] {
    def search(query: ObjectQuery): Page[Obj]
    def findByFolder(folderId: Long): Array[Obj]
    def search(query: String): Array[Obj]
    def typeAhead(query: String): Array[String]
    def findByType(id: Long): List[Obj]
    def byEmail(email: String): Option[Obj]
}

@Component
@Transactional
class ObjectDaoImpl extends GenericDaoImpl[Obj, java.lang.Long] with ObjectDao with Logging {

    def search(q: ObjectQuery): Page[Obj] = {
        val s = new Search(classOf[Obj]).setMaxResults(q.pageSize).setFirstResult(q.offset).addSort("name", false)
        q.objectType.foreach(t => {
            s.addFetch("objectType")
            s.addFilterEqual("objectType.id", t)
        })
        val result = searchAndCount(s)
        Page(result.getResult, q.pageNumber, q.pageSize, result.getTotalCount)
    }

    override def byEmail(email: String): Option[Obj] = {
        val search = new Search(classOf[Obj])
        search.addFilterEqual("email", email)
        Option(super.searchUnique(search))
    }

    def findByType(id: Long): List[Obj] = {
        getSession
          .createCriteria(classOf[Obj])
          .setFetchMode("objectType", FetchMode.EAGER)
          .add(Restrictions.eq("objectType.id", id))
          .setMaxResults(2000)
          .list()
          .asScala
          .map(_.asInstanceOf[Obj])
          .toList
    }

    override def typeAhead(query: String): Array[String] = {
        getSession
          .createSQLQuery("select name from items where name like ?")
          .setString(0, query + "%")
          .setMaxResults(20)
          .list()
          .asScala
          .map(_.asInstanceOf[String])
          .toArray
    }

    override def search(query: String): Array[Obj] = {
        getSession
          .createCriteria(classOf[Obj])
          .add(Restrictions.like("name", "%" + query))
          .setMaxResults(20)
          .list()
          .asScala
          .map(_.asInstanceOf[Obj])
          .toArray
    }

    override def findByFolder(folderId: Long): Array[Obj] = {
        getSession
          .createSQLQuery("select i.* from items i join categories_items ci on i.id=ci.item where ci.category=?")
          .addEntity("i", classOf[Obj])
          .setLong(0, folderId)
          .setMaxResults(20)
          .list()
          .asScala
          .map(_.asInstanceOf[Obj])
          .toArray
    }

    //    @Override
    //    public int count(long submissionId) {
    //        return super.count(new Search(Comment.class).addFilterEqual("submissionId", submissionId));
    //    }
    //
    //    @Override
    //    public SearchResult < Comment > findBySubmissionId (long submissionId, ListQueryParams listQueryParams) {
    //        return searchAndCount(new Search(Comment.class)
    //        .addFilterEqual("submissionId", submissionId)
    //          .addFilterIn("status", (Object[]) CommentStatus.visibleStatuses())
    //        .addSort("createdOnTimestamp", true)
    //          .setFirstResult(listQueryParams.getOffset())
    //          .setMaxResults(listQueryParams.getPerPage()));
    //    }
    //
    //    @Override
    //    public SearchResult < Comment > findByUserId (long userId, ListQueryParams listQueryParams) {
    //        return searchAndCount(new Search(Comment.class).addFilterEqual("userId", userId).addFilterIn("status",
    //            (Object[]) CommentStatus.visibleStatuses()) );
    //    }
}
