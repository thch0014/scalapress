package com.liferay.scalapress.dao

import org.springframework.stereotype.Component
import org.hibernate.criterion.Restrictions
import scala.collection.JavaConverters._
import com.liferay.scalapress.domain.{SavedSearch, Obj}
import org.hibernate.FetchMode
import com.googlecode.genericdao.search.Search
import com.liferay.scalapress.Logging
import org.springframework.transaction.annotation.Transactional

/** @author Stephen Samuel */

trait ObjectDao extends GenericDao[Obj, java.lang.Long] {

    def findByFolder(folderId: Long): Array[Obj]
    def search(query: String): Array[Obj]
    def typeAhead(query: String): Array[String]
    def findByType(id: Long): List[Obj]
    def search(search: SavedSearch): List[Obj]
    def byEmail(email: String): Option[Obj]
}

@Component
@Transactional
class ObjectDaoImpl extends GenericDaoImpl[Obj, java.lang.Long] with ObjectDao with Logging {

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

    override def search(ss: SavedSearch): List[Obj] = {

        val search = new Search(classOf[Obj])
        search.addFetch("objectType")

        Option(ss.searchFolder).foreach(c => {
            search.addFetch("folders")
            search.addFilterIn("folders", ss.searchFolder)
        })

        if (ss.objectType != null) {
            search.addFilterEqual("objectType", ss.objectType)
        }

        search.addFilterEqual("status", ss.status)

        Option(ss.name) match {
            case None =>
            case Some(name) if (name.startsWith("-")) =>
                val value = "%" + name.substring(1) + "%"
                search.addFilterLike("name", name)
            case Some(name) =>
                val value = "%" + name + "%"
                search.addFilterLike("name", name)
        }

        if (ss.keywords != null) {
            search.addFilterEqual("keywords", "%" + ss.keywords + "%")
        }

        if (ss.minPrice > 0) {
            search.addFilterGreaterOrEqual("sellPrice", ss.minPrice)
        }

        if (ss.maxPrice > 0) {
            search.addFilterLessOrEqual("sellPrice", ss.maxPrice)
        }

        if (ss.newerThanTimestamp > 0) {
            search.addFilterGreaterOrEqual("dateCreated", ss.newerThanTimestamp)
        }

        if (ss.status != null) {
            search.addFilterEqual("status", ss.status)
        }

        if (ss.imageOnly) {
            search.addFilterNotEmpty("images")
        }

        if (ss.inStockOnly) {
            search.addFilterGreaterThan("stock", 0)
        }

        super.search(search)
    }
}
