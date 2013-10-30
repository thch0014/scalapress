package com.cloudray.scalapress.search

import com.cloudray.scalapress.item._
import org.springframework.beans.factory.annotation.Autowired
import com.googlecode.genericdao.search.Search

/** @author Stephen Samuel
  *
  *         An implementation of SearchService that simply defers to the backing database.
  * */
class DatabaseSearchService extends SearchService {

  @Autowired var objectDao: ItemDao = _

  def search(search: SavedSearch): SearchResult = {

    val q = new ItemQuery().withStatus(Item.STATUS_LIVE).withName(search.name)

    if (search.objectType != null) {
      q.withTypeId(search.objectType.id)
    }

    if (search.objectType != null) {
      q.withPageSize(search.maxResults)
    }

    if (search.minPrice > 0) {
      q.withMinPrice(search.minPrice)
    }
    if (search.maxPrice > 0) {
      q.withMaxPrice(search.maxPrice)
    }

    q.withSort(search.sortType)

    val objs = objectDao.search(q)
    val refs = objs.results.map(obj =>
      ItemRef(obj.id, obj.objectType.id, obj.name, obj.status, Map.empty, Nil, obj.prioritized))
    new SearchResult(refs, Nil, -1)
  }

  def count: Long = objectDao.count(new Search(classOf[Item]))
  def contains(id: String): Boolean = objectDao.find(id.toLong) != null
  def typeahead(q: String, limit: Int): Seq[ItemRef] = Nil

  // the following are no-op methods, as there is no "index".
  def remove(id: String): Unit = {}
  def index(obj: Item): Unit = {}
  def stats: Map[String, String] = Map.empty

}
