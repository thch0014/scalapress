package com.cloudray.scalapress.search

import com.cloudray.scalapress.obj.{ObjectQuery, ObjectDao, Obj}
import org.springframework.beans.factory.annotation.Autowired
import com.googlecode.genericdao.search.Search

/** @author Stephen Samuel
  *
  *         An implementation of SearchService that searches the database for item titles only.
  **/
class DatabaseSearchService extends SearchService {

  @Autowired var objectDao: ObjectDao = _

  def stats: Map[String, String] = Map.empty
  def typeahead(q: String, limit: Int): Seq[ObjectRef] = Nil
  def search(search: SavedSearch): SearchResult = {
    val objs = objectDao.search(new ObjectQuery().withStatus(Obj.STATUS_LIVE).withName(search.name))
    val refs = objs.results.map(obj =>
      ObjectRef(obj.id, obj.objectType.id, obj.name, obj.status, Map.empty, Nil, obj.prioritized))
    new SearchResult(refs, Nil, -1)
  }

  def count: Long = objectDao.count(new Search(classOf[Obj]))
  def contains(id: String): Boolean = objectDao.find(id.toLong) != null

  // the following are no-op methods, as there is no "index". This implementation
  // just reads straight from the db
  def remove(id: String): Unit = {}
  def index(objs: Seq[Obj]): Unit = {}
  def index(obj: Obj): Unit = {}
}
