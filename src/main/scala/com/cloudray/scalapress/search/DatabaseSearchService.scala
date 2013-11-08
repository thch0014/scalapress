package com.cloudray.scalapress.search

import com.cloudray.scalapress.item._
import org.springframework.beans.factory.annotation.Autowired

/** @author Stephen Samuel
  *
  *         An implementation of SearchService that simply defers to the backing database.
  * */
class DatabaseSearchService extends SearchService {

  @Autowired
  var objectDao: ItemDao = _

  def search(search: Search): SearchResult = {

    val q = new ItemQuery().withStatus(Item.STATUS_LIVE)

    search.name.foreach(q.withName)
    search.itemType.foreach(itemType => q.withTypeId(itemType.id))

    q.withPageSize(search.maxResults)

    if (search.minPrice > 0) {
      q.withMinPrice(search.minPrice)
    }

    if (search.maxPrice > 0) {
      q.withMaxPrice(search.maxPrice)
    }

    q.withSort(search.sort)

    val objs = objectDao.search(q)
    val refs = objs.results.map(obj =>
      ItemRef(obj.id, obj.objectType.id, obj.name, obj.status, Map.empty, Nil, obj.prioritized))
    new SearchResult(refs, Nil, -1)
  }

  def count: Long = objectDao.count(new com.googlecode.genericdao.search.Search(classOf[Item]))
  def contains(id: String): Boolean = objectDao.find(id.toLong) != null
  def typeahead(q: String, limit: Int): Seq[ItemRef] = Nil

  // the following are no-op methods, as there is no "index".
  def remove(id: String): Unit = {}
  def index(obj: Item): Unit = {}
  def stats: Map[String, String] = Map.empty

}
